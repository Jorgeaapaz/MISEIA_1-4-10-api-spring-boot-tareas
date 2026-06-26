@~/.claude/prompts/new_functionality_prompt_spec.md

# Add .env.example Configuration Template

## Role
Act as a Software Developer and DevOps Engineer, expert in Spring Boot project configuration and secret management best practices.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
Git remote: GitHub (`Jorgeaapaz/MISEIA_1-4-10-api-spring-boot-tareas`) and GitLab (`jorgeaapaz/MISEIA_1-4-10-api-spring-boot-tareas`)

**Non-compliant item:** `dc_env_example`  
The project has a `.gitignore` that correctly excludes `.env` and `application-prod.properties`, but there is no `.env.example` file at the project root. This means anyone cloning the repo does not know which environment variables are required to run in production (MySQL mode).

The `pom.xml` includes `mysql-connector-j` as a runtime dependency, and `application.properties` currently hardcodes H2 in-memory settings. A production profile would require MySQL credentials via environment variables.

## Task
Create a `.env.example` file at the project root listing all environment variables needed for production (MySQL) configuration, without real values. Then update `README.md` to reference it.

### .env.example Guidelines
- Include all variables needed for a production MySQL-backed run
- Use placeholder values (e.g., `your_db_password_here`)
- Add inline comments explaining each variable
- Group variables logically (Database, Server, JPA)
- Do NOT include the H2 dev variables (those are hardcoded in `application.properties` for dev)

### README Update Guidelines
- Add a "Configuration" or "Environment Variables" subsection under "Getting Started"
- Reference `.env.example` with: "Copy `.env.example` to `.env` and fill in your values"
- Show how to pass env vars to `mvn spring-boot:run` using `-D` flags or Spring profiles

## Output Format
1. File: `.env.example` at project root
2. Updated section in `README.md`

## Examples and Steps to Follow
1. Read `pom.xml` to confirm MySQL connector presence
2. Read `application.properties` to identify property names used
3. Create `application-prod.properties.example` or add inline comments to `.env.example`
4. The env vars should map to: `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
5. Add Spring profile activation var: `SPRING_PROFILES_ACTIVE=prod`

Example `.env.example`:
```
# Spring Boot active profile (use 'prod' for MySQL, 'default' for H2)
SPRING_PROFILES_ACTIVE=prod

# MySQL Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=tareasdb
DB_USERNAME=your_db_username_here
DB_PASSWORD=your_db_password_here

# Server
SERVER_PORT=8080
```

## Output Checklist and Guardrails
- [ ] `.env.example` exists at project root
- [ ] All placeholder values are clearly fake (no real credentials)
- [ ] README references `.env.example`
- [ ] No real secrets committed
- [ ] `.gitignore` already excludes `.env` — verify it's not accidentally including `.env.example`
- [ ] Run `git log -p | grep -i 'password\|secret\|api_key'` to verify no secrets leaked

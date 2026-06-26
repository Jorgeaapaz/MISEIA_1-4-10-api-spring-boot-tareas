@~/.claude/prompts/new_functionality_prompt_spec.md

# Configure Checkstyle Linter for Spring Boot Project

## Role
Act as a Java Software Engineer and build tool expert, specializing in Maven plugins and code quality enforcement.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
Build tool: Maven 3.9.6 (via wrapper `./mvnw`)

**Non-compliant item:** `cq_linter_configurado`  
The `pom.xml` has no linter or formatter plugin configured. Evaluation requires a versioned linter/formatter configuration (`checkstyle`, `spotless`, `gofmt`, etc.) committed to the repo.

The project has 5 Java source files and follows Google/Sun Java conventions already. Adding Checkstyle with a standard ruleset will formalize this.

## Task
Add Checkstyle Maven plugin to `pom.xml` with a versioned configuration file committed to the repo. The linter must:
1. Run automatically as part of `mvn verify` (not just `mvn checkstyle:check`)
2. Use a standard ruleset (Google Java Style or Sun checks)
3. Fail the build if violations are found
4. Have its configuration file committed to the repo (not downloaded from external URL)

### Checkstyle Guidelines
- Plugin: `maven-checkstyle-plugin` version `3.3.1` or higher
- Ruleset: Use `google_checks.xml` (bundled with Checkstyle) OR create a custom `checkstyle.xml` in `src/main/resources/checkstyle/`
- Bind to `verify` lifecycle phase
- Set `<failOnViolation>true</failOnViolation>` and `<consoleOutput>true</consoleOutput>`
- Exclude generated sources and `target/` directory
- Allow max line length 120 chars (per Java rules for this project)

### pom.xml Update Guidelines
- Add plugin under `<build><plugins>` section
- Pin the Checkstyle version explicitly (`<checkstyleVersion>10.12.7</checkstyleVersion>`)
- Do not use `<phase>process-sources</phase>` — use `verify` so tests run first
- Add `<sourceDirectory>` to scan only `src/main/java`

## Output Format
1. Updated `pom.xml` with `maven-checkstyle-plugin` configured
2. `src/main/resources/checkstyle/checkstyle.xml` (or reference to bundled Google checks)
3. Verify `./mvnw verify` passes with no violations

## Examples and Steps to Follow
1. Read current `pom.xml` to understand existing plugins
2. Add Checkstyle plugin with correct configuration
3. Run `./mvnw checkstyle:check` to catch violations before wiring to build
4. Fix any violations in existing Java files (likely minor spacing/import ordering issues)
5. Run `./mvnw verify` to confirm full build + tests + lint passes

Example plugin block:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.3.1</version>
    <configuration>
        <configLocation>google_checks.xml</configLocation>
        <consoleOutput>true</consoleOutput>
        <failsOnError>true</failsOnError>
        <failOnViolation>true</failOnViolation>
        <violationSeverity>warning</violationSeverity>
        <includeTestSourceDirectory>true</includeTestSourceDirectory>
    </configuration>
    <executions>
        <execution>
            <id>validate</id>
            <phase>verify</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Output Checklist and Guardrails
- [ ] `maven-checkstyle-plugin` present in `pom.xml` with pinned version
- [ ] Configuration file committed to repo (not remote URL)
- [ ] `./mvnw verify` passes (tests + lint both green)
- [ ] No violations in existing source files
- [ ] Checkstyle config file path is relative (not absolute)
- [ ] Commit the change with a descriptive message

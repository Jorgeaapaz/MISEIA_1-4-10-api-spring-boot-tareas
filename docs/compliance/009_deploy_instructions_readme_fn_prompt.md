@~/.claude/prompts/new_functionality_prompt_spec.md

# Add Deployment Instructions to README

## Role
Act as a DevOps Engineer and Software Architect who writes clear, verifiable cloud deployment documentation for Spring Boot applications.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
Predecessor: `011_ci_pipeline_github_fn_prompt.md` (GitHub CI/CD must be configured first — the deploy instructions reference the automated pipeline and the running URL)

**Non-compliant item:** `dc_instrucciones_deploy`  
The README has comprehensive local dev instructions but no deployment section. Evaluation requires verifiable deployment steps (Dockerfile + command, deploy script, or cloud instructions).

The GitHub Actions workflow (created in task 011) deploys to:
- VM: `gcvmuser@34.174.56.186`
- Directory: `~/MISEIA1-4-10_api-spring-boot-tareas`
- Domain: `api-tareas.deviaaps.com`
- Port: `30001`
- Traefik network: `miseia-net`
- Docker containerized Spring Boot JAR

## Task
Add a "## Deployment" section to `README.md` that documents:
1. The production deployment architecture (Docker + Traefik + GCP VM)
2. How the automated GitHub Actions pipeline deploys (CI → build JAR → Docker build → SSH deploy)
3. The public URL where the API is accessible
4. Manual deployment steps as fallback (for cases where CI is not available)
5. How to verify the deployment is working

### Deployment Section Guidelines
- Reference GitHub Actions as the primary deployment method
- Include the public URL: `https://api-tareas.deviaaps.com`
- Show how to verify: `curl https://api-tareas.deviaaps.com/api/tareas`
- Include a `Dockerfile` in the project root (build if not present)
- Document the Traefik routing labels used in docker-compose
- Include SSH access command for manual verification

### Dockerfile Guidelines (if not present)
- Base image: `eclipse-temurin:17-jre-alpine` (small, JRE-only)
- Copy the fat JAR from `target/api-tareas-1.0.0.jar`
- Expose port 8080
- Use `ENTRYPOINT ["java", "-jar", "/app.jar"]`
- Add `ENV SPRING_PROFILES_ACTIVE=prod` as default

## Output Format
1. `Dockerfile` at project root (create if not exists)
2. Updated `README.md` with "## Deployment" section including:
   - Architecture overview
   - Public URL
   - Automated CI/CD note (references GitHub Actions workflow)
   - Manual deployment steps
   - Verification commands

## Examples and Steps to Follow
1. Check if `Dockerfile` already exists
2. Create `Dockerfile` using `eclipse-temurin:17-jre-alpine`
3. Add "## Deployment" section to README
4. Include the public URL `https://api-tareas.deviaaps.com`
5. Document verification with curl
6. Commit all changes

Example README section:
```markdown
## Deployment

### Public URL
The API is deployed and accessible at:  
**https://api-tareas.deviaaps.com/api/tareas**

### Architecture
The production deployment runs on a GCP VM (`us-south1-c`) with:
- **Docker** containerizing the Spring Boot JAR
- **Traefik v3.3** handling HTTPS termination (wildcard `*.deviaaps.com` cert via Let's Encrypt)
- **GitHub Actions** automating build and deployment on every push to `master`

### Automated Deployment (GitHub Actions)
Every push to `master` triggers the CI/CD pipeline:
1. Runs `./mvnw verify` (tests + linter + coverage)
2. Builds the fat JAR
3. Builds and pushes a Docker image
4. SSH-deploys to the GCP VM via `docker compose up -d`

See `.github/workflows/ci-cd.yml` for the full pipeline.

### Verify Deployment
```bash
curl https://api-tareas.deviaaps.com/api/tareas
# Expected: 200 OK with empty array []
```

### Manual Deployment
```bash
# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t api-tareas:latest .

# Deploy to VM (requires SSH key)
ssh -i C:\ubuntuiso\.ssh\vboxuser gcvmuser@34.174.56.186 \
  "cd ~/MISEIA1-4-10_api-spring-boot-tareas && docker compose up -d"
```
```

## Output Checklist and Guardrails
- [ ] `Dockerfile` created and builds successfully (`docker build .`)
- [ ] Public URL `https://api-tareas.deviaaps.com` documented in README
- [ ] Automated CI/CD pipeline referenced
- [ ] Manual deployment steps included
- [ ] Verification command (`curl`) included
- [ ] No real SSH keys or credentials in the file
- [ ] Commit with descriptive message

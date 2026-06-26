@~/.claude/prompts/new_functionality_prompt_spec.md

# Create a GitHub CI/CD Pipeline and Deploy App to VM at Google Cloud

## Role
Act as a Software Architect, you are an expert in GitHub Actions and Google Cloud Services.

## Task
Create GitHub Actions that compile, test, and deploy the Spring Boot API to `ssh -i C:\ubuntuiso\.ssh\vboxuser gcvmuser@34.174.56.186` in the directory `~/MISEIA1-4-10_api-spring-boot-tareas`. Tests and build must be done in GitHub Actions. The service must be created in the remote Ubuntu VM in Docker.

The app must be accessible through Traefik using the domain `api-tareas.deviaaps.com`, port `30001`, using the Traefik wildcard `*.deviaaps.com`.

Use `/gh-cli` and gcloud for all secrets required.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
GitHub remote: `https://github.com/Jorgeaapaz/MISEIA_1-4-10-api-spring-boot-tareas`

**Non-compliant items addressed:**
- `cq_ci_funcional` — No GitHub Actions CI pipeline exists
- `fn_deploy_publico_accesible` — No public URL; app not deployed to accessible endpoint

**VM Infrastructure:**
- VM: `gcvmuser@34.174.56.186`
- SSH key: `C:\ubuntuiso\.ssh\vboxuser` (private key)
- Docker network: `miseia-net` (already running Traefik v3.3 + other services)
- Traefik wildcard cert: `*.deviaaps.com` (already configured on VM)
- App domain: `api-tareas.deviaaps.com`
- Internal port: `8080` (Spring Boot default)
- Traefik entry port: `30001` (as required by evaluation)
- Deploy directory on VM: `~/MISEIA1-4-10_api-spring-boot-tareas`

**Predecessor tasks that must be completed first:**
- `001_env_example_config_fn_prompt.md` — `.env.example` must exist
- `002_linter_checkstyle_config_fn_prompt.md` — Checkstyle must be configured in `pom.xml`
- `003_coverage_jacoco_report_fn_prompt.md` — JaCoCo must be configured in `pom.xml`

## GitHub Actions Pipeline Requirements

### Workflow: `.github/workflows/ci-cd.yml`
**Trigger:** Push to `master` branch and Pull Requests to `master`

**Jobs:**
1. `test` — Run `./mvnw test` (all 13 integration tests must pass)
2. `lint` — Run `./mvnw checkstyle:check`
3. `coverage` — Run `./mvnw verify` and upload JaCoCo report as artifact
4. `build` — Run `./mvnw clean package -DskipTests` (tests already run in job 1), upload JAR as artifact
5. `deploy` — Only on push to `master` (not PRs): SSH to VM, build Docker image, run `docker compose up -d`
   - `NODE_ENV=production` only in the build step, not as env-level variable

### Docker Setup on VM
Create on the VM at `~/MISEIA1-4-10_api-spring-boot-tareas/`:
- `Dockerfile` — builds the Spring Boot JAR into a container
- `docker-compose.yml` — defines the service with Traefik labels for `api-tareas.deviaaps.com`

### Traefik Labels for docker-compose.yml
```yaml
labels:
  - "traefik.enable=true"
  - "traefik.http.routers.api-tareas.rule=Host(`api-tareas.deviaaps.com`)"
  - "traefik.http.routers.api-tareas.entrypoints=websecure"
  - "traefik.http.routers.api-tareas.tls=true"
  - "traefik.http.routers.api-tareas.tls.certresolver=cloudflare"
  - "traefik.http.services.api-tareas-svc.loadbalancer.server.port=8080"
networks:
  - miseia-net
```

Note: `30001` is the evaluation-required external port. Add an additional entrypoint in Traefik config OR expose port `30001` directly:
```yaml
ports:
  - "30001:8080"
```

### GitHub Secrets Required
Add via `/gh secret set` or GitHub web UI:
- `VM_SSH_PRIVATE_KEY` — content of `C:\ubuntuiso\.ssh\vboxuser` (private key)
- `VM_HOST` — `34.174.56.186`
- `VM_USER` — `gcvmuser`
- `VM_DEPLOY_DIR` — `~/MISEIA1-4-10_api-spring-boot-tareas`

### Dockerfile (at project root)
```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/api-tareas-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Output Format
1. `.github/workflows/ci-cd.yml` — full CI/CD pipeline
2. `Dockerfile` at project root
3. `docker-compose.yml` at project root (for VM deployment, separate from any existing one)
4. GitHub Secrets configured via `/gh`
5. Pipeline runs successfully — all stages green
6. App accessible at `https://api-tareas.deviaaps.com/api/tareas` (returns `[]`)
7. Updated `README.md` with public URL and CI/CD badge

## Examples and Steps to Follow
1. Create `Dockerfile` and `docker-compose.yml`
2. Create `.github/workflows/ci-cd.yml`
3. Add GitHub secrets using `/gh secret set VM_SSH_PRIVATE_KEY`, etc.
4. Push to GitHub — verify Actions run
5. SSH to VM and verify container is running: `docker ps | grep api-tareas`
6. Verify URL: `curl https://api-tareas.deviaaps.com/api/tareas`
7. Add GitHub Actions badge to README: `[![CI/CD](https://github.com/Jorgeaapaz/MISEIA_1-4-10-api-spring-boot-tareas/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/Jorgeaapaz/MISEIA_1-4-10-api-spring-boot-tareas/actions/workflows/ci-cd.yml)`

## Output Checklist and Guardrails
- [ ] `.github/workflows/ci-cd.yml` created with 5 jobs
- [ ] All test, lint, and coverage jobs run without `-DskipTests` (except final build job)
- [ ] `NODE_ENV=production` appears ONLY in the build step script, NOT as a job-level `env:`
- [ ] Deploy job only runs on push to `master` (not on PRs)
- [ ] SSH key stored as GitHub Secret (not committed to repo)
- [ ] `Dockerfile` builds successfully
- [ ] `docker-compose.yml` has correct Traefik labels for `api-tareas.deviaaps.com`
- [ ] App accessible at `https://api-tareas.deviaaps.com/api/tareas`
- [ ] Port `30001` exposed/routed correctly
- [ ] README updated with public URL and Actions badge
- [ ] All secrets set via `/gh` — no credentials in any committed file

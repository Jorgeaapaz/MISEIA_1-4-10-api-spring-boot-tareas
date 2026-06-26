@~/.claude/prompts/new_functionality_prompt_spec.md

# Fix GitLab CI Pipeline — Run Tests and Linter on Every Push

## Role
Act as a DevOps Engineer and GitLab CI/CD expert, specializing in Java/Maven Spring Boot pipelines.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
GitLab remote: `https://gitlab.codecrypto.academy/jorgeaapaz/MISEIA_1-4-10-api-spring-boot-tareas`  
Use `/glab` skill for all GitLab operations.

**Non-compliant item:** `cq_ci_funcional`  
The current `.gitlab-ci.yml` has a single `build` stage that runs:
```
mvn clean package -DskipTests
```
Tests are **skipped** (`-DskipTests`), which means the CI pipeline does not validate code correctness. Additionally, there is no linter stage. The last build must be green with tests passing.

Predecessor tasks that must be completed first:
- `002_linter_checkstyle_config_fn_prompt.md` — Checkstyle must be configured in `pom.xml`
- `003_coverage_jacoco_report_fn_prompt.md` — JaCoCo must be configured in `pom.xml`

## Task
Rewrite `.gitlab-ci.yml` to:
1. Add a `test` stage that runs `mvn test` (all 13 integration tests must pass)
2. Add a `lint` stage that runs `mvn checkstyle:check` (or runs as part of `mvn verify`)
3. Add a `build` stage that runs `mvn clean package` (with tests, not skipping)
4. Use `NODE_ENV=production` ONLY for the `mvn package` build command (not as a job-level variable)
5. Cache Maven `.m2/repository` across all stages

### GitLab CI Guidelines
- Use `/glab` for all GitLab CLI operations (checking pipeline status, viewing logs)
- Image: `maven:3.9-eclipse-temurin-17`
- Stages order: `test` → `lint` → `build`
- Cache key: `"$CI_COMMIT_REF_SLUG-m2"` shared across all stages
- `NODE_ENV=production` must only appear in the `script:` section of the build job, not as a job-level `variables:` block
- The `build` job must NOT use `-DskipTests`
- Upload JAR artifact: `target/*.jar` with `expire_in: 1 week`
- The pipeline must be green after the fix — verify with `/glab ci status`

### Stage Definitions
```yaml
# test stage: validate all tests pass
test:
  stage: test
  script:
    - mvn test

# lint stage: validate code style
lint:
  stage: lint  
  script:
    - mvn checkstyle:check

# build stage: package with NODE_ENV=production only in script
build:
  stage: build
  script:
    - NODE_ENV=production mvn clean package
  artifacts:
    paths:
      - target/*.jar
```

## Output Format
1. Updated `.gitlab-ci.yml` with 3 stages (test, lint, build)
2. Verify pipeline passes using `/glab ci status` or `/glab pipeline list`
3. Confirm all 3 stages are green

## Examples and Steps to Follow
1. Read current `.gitlab-ci.yml` to understand what exists
2. Rewrite with the 3-stage structure above
3. Push changes: `git add .gitlab-ci.yml && git commit -m "fix: run tests and linter in CI pipeline"`
4. Push to GitLab remote
5. Use `/glab` to check pipeline status
6. Confirm all stages pass (especially `test` with 13/13 tests)

## Output Checklist and Guardrails
- [ ] `stages: [test, lint, build]` defined
- [ ] `test` stage runs `mvn test` without `-DskipTests`
- [ ] `lint` stage runs Checkstyle
- [ ] `build` stage: `NODE_ENV=production` is ONLY in `script:`, NOT in `variables:`
- [ ] No job uses `-DskipTests`
- [ ] Maven cache shared across all stages with same key
- [ ] Pipeline is green (verified with `/glab`)
- [ ] Artifacts uploaded for the `build` stage JAR
- [ ] Commit and push completed

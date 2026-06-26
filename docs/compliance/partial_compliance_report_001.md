# Compliance Report — Partial — 001

**Project:** API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
**Repo:** `MISEIA_1-4-10-api-spring-boot-tareas`  
**Audit Date:** 2026-06-26  
**Result:** ⚠️ PARTIAL COMPLIANCE — 24 ✅ / 4 ⚠️ / 1 ❌ / 1 N/A (out of 30 criteria)

---

## Summary Score

| Category | ✅ Compliant | ⚠️ Partial | ❌ Non-Compliant | N/A |
|---|---|---|---|---|
| Functionality (10) | 7 | 1 | 1 | 1 |
| Code Quality (10) | 7 | 3 | 0 | 0 |
| Documentation (10) | 10 | 0 | 0 | 0 |
| **Total (30)** | **24** | **4** | **1** | **1** |

---

## Detailed Compliance Table

### Funcionalidad y cumplimiento del enunciado

| ID | Criteria | Status | Evidence |
|---|---|---|---|
| `fn_se_instala` | README has install instructions | ✅ COMPLIANT | `./mvnw test`, `./mvnw spring-boot:run` documented. Maven Wrapper 3.9.6 included — no Maven install required. |
| `fn_arranca_local` | App starts with documented command on known port | ✅ COMPLIANT | `./mvnw spring-boot:run` → `http://localhost:8080/api/tareas` documented in README §5. |
| `fn_flujo_principal_funciona` | Full CRUD end-to-end | ✅ COMPLIANT | 6 endpoints (GET/POST/PUT/DELETE + filter). 12 integration tests covering all flows. Test 12: `crudCompleto_flujoIntegrado` verifies full cycle. |
| `fn_persistencia_efectiva` | Data survives server restart | ⚠️ PARTIAL | Dev: H2 in-memory — data lost on restart. Prod: MySQL config exists in `.env.example` and `application-prod.properties` but `docker-compose.yml` has no MySQL service — requires external DB. ADR-0001 documents this trade-off. |
| `fn_validaciones_de_entrada` | Inputs validated, 400 on invalid | ✅ COMPLIANT | `@NotBlank` + `@Size(max=200)` on `titulo`; `@Size(max=1000)` on `descripcion`; `@Valid` in controller. DB-level constraints mirror. Test 2 asserts `HTTP 400`. |
| `fn_manejo_errores_consistente` | Consistent error responses with status codes | ✅ COMPLIANT | 201 (POST), 204 (DELETE), 400 (validation), 404 (not found) — all explicitly coded and tested. No silent 500s. |
| `fn_funciones_completas_del_enunciado` | All enunciado features implemented | ✅ COMPLIANT | Full CRUD + filter by state. All 6 endpoints tested. Postman collection included. |
| `fn_features_extra_pertinentes` | Extra features relevant to domain | ✅ COMPLIANT | Filter by completion status (`GET /estado/{completada}`), audit timestamps (`@PrePersist/@PreUpdate`), data seeding on startup (`DataSeeder.java`), Postman collection. |
| `fn_estados_intermedios_ui` | UI handles loading/error/empty states | N/A | Backend API only — no UI in scope. |
| `fn_deploy_publico_accesible` | Public URL documented in README | ❌ NON-COMPLIANT | Traefik label `api-tareas.deviaaps.com` exists in `docker-compose.yml` and deploy job runs in CI, but **no public URL is documented in README**. No health-check or access verification instructions. |

---

### Calidad de código y arquitectura

| ID | Criteria | Status | Evidence |
|---|---|---|---|
| `cq_estructura_carpetas_clara` | Folder structure reflects architecture | ✅ COMPLIANT | `controller/`, `service/`, `repository/`, `model/` under `com.tareas.api`. `docs/decisions/`, `docs/compliance/`. README §2 documents full tree. |
| `cq_nombres_descriptivos` | Descriptive names throughout | ✅ COMPLIANT | `listarTodas()`, `obtenerPorId()`, `eliminar()`, `fechaCreacion`, `completada` — all Spanish domain names, no `tmp`/`data2`. |
| `cq_separacion_responsabilidades` | Layers properly separated | ✅ COMPLIANT | Controller (HTTP mapping) → Service (business logic) → Repository (data access). Constructor injection everywhere. No cross-layer imports. |
| `cq_dependencias_lockeadas` | Fixed versions in pom.xml | ✅ COMPLIANT | `spring-boot-starter-parent:3.2.5`, `java.version=17`, `checkstyle-plugin:3.3.1`, `jacoco:0.8.12`. Maven Wrapper pins `mvn:3.9.6`. |
| `cq_tests_minimos` | Automated tests, one-command runnable | ✅ COMPLIANT | 15 tests (12 integration + 2 seeder + 1 smoke). `./mvnw test`. All passing. `@SpringBootTest(RANDOM_PORT)` with `TestRestTemplate`. |
| `cq_linter_configurado` | Linter configured and versioned | ⚠️ PARTIAL | `checkstyle.xml` committed, `maven-checkstyle-plugin:3.3.1` in `pom.xml`, runs in CI. **Gap:** only 2 rules (`LineLength:120`, `EmptyStatement`). Missing naming conventions, import ordering, visibility modifiers. |
| `cq_sin_secretos_en_repo` | No secrets/credentials in repo | ✅ COMPLIANT | `.gitignore` excludes `.env`, `application-prod.properties`. `.env.example` uses placeholders only. CI secrets via `${{ secrets.VM_SSH_PRIVATE_KEY }}`. |
| `cq_arquitectura_razonada` | Explicit layered architecture | ✅ COMPLIANT | MVC clearly documented. Mermaid component diagram in README §3.5. 3 ADRs document architectural choices. Constructor injection. `@PrePersist`/`@PreUpdate` lifecycle callbacks. |
| `cq_cobertura_alta` | Coverage >60% reported | ⚠️ PARTIAL | JaCoCo configured with `minimum=0.60` threshold. Actual: **96.6% global, 100% domain code** (per `target/site/jacoco/jacoco.csv`). **Gap:** No coverage badge or report link in README. Report only in `target/` (gitignored). |
| `cq_ci_funcional` | CI pipeline runs tests+linter on every push | ⚠️ PARTIAL | GitHub Actions (`.github/workflows/ci-cd.yml`) runs `test` → `lint` → `coverage` → `build` → `deploy` on every push to master. **Gap:** Last CI build status unknown (no status badge in README). GitLab CI (`.gitlab-ci.yml`) exists but agent noted it may be outdated. |

---

### Documentación y decisiones

| ID | Criteria | Status | Evidence |
|---|---|---|---|
| `dc_readme_presente` | README with what/install/run/endpoints | ✅ COMPLIANT | 12-section README. §1 endpoints, §2 structure, §3 architecture, §5 quick-start with commands, §6 request/response examples. |
| `dc_env_example` | `.env.example` with all required vars | ✅ COMPLIANT | Root `.env.example` documents: `DB_HOST`, `DB_PORT`, `DB_NAME`, `SPRING_DATASOURCE_URL/USERNAME/PASSWORD`, `SERVER_PORT`, `SPRING_PROFILES_ACTIVE`. Placeholders only. |
| `dc_comandos_verificacion` | Exact commands to verify work in README | ✅ COMPLIANT | `./mvnw test`, `./mvnw verify`, `./mvnw spring-boot:run`, H2 console URL, API base URL — all in README §5. |
| `dc_seccion_uso` | Real usage examples (request/response) | ✅ COMPLIANT | README §6 has 6 examples: create, filter, update, delete, 400 error, 404 error — with request body, status code, and response body. |
| `dc_diagrama_arquitectura` | Architecture diagram | ✅ COMPLIANT | Mermaid `graph TD` in README §3.5: Client → Controller → @Valid → Service → Repository → Entity → H2/MySQL (7 nodes, full flow). |
| `dc_decisiones_documentadas` | 2+ real trade-off decisions documented | ✅ COMPLIANT | ADR-0001 (H2 vs MySQL), ADR-0002 (derived queries vs @Query), ADR-0003 (integration vs unit tests). Each with options considered + rationale. |
| `dc_cambios_ia_documentados` | AI usage documented | ✅ COMPLIANT | `RETROSPECTIVA-2026-06-26.md` documents model (GitHub Copilot — Claude Sonnet 4.6), prompts used, and methodology. Prior session retrospectives also present. |
| `dc_adrs_o_decision_log` | ADRs with context/decision/consequences | ✅ COMPLIANT | `docs/decisions/` — 3 ADRs with full format: Date, Status, Deciders, Context, Problem, Options (A/B/C), Decision Outcome, Positive/Negative Consequences, Measurements. |
| `dc_justificacion_cuantitativa` | At least one decision justified with numbers | ✅ COMPLIANT | ADR-0001: H2=38.9s vs Testcontainers=55–65s (~35–40% faster). ADR-0003: timing table (context ~8s, per-test ~2.4s). README §7.2: NFR-PERF-001 (p95<100ms), NFR-PERF-002 (startup ~8s). |
| `dc_instrucciones_deploy` | Deploy instructions (Dockerfile + commands) | ✅ COMPLIANT | `Dockerfile` (eclipse-temurin:17, EXPOSE 8080), `docker-compose.yml` (Traefik labels, port 30001), CI deploy job (scp → docker build → docker compose up -d on GCP VM). |

---

## Non-Compliant & Partial Items — Remediation Plan

### ❌ `fn_deploy_publico_accesible` — Add Public URL to README

**Gap:** The app is deployed to `api-tareas.deviaaps.com` via Traefik (evidenced in `docker-compose.yml` labels and GitHub Actions deploy job), but this URL is not documented in README.

**Fix:**
```markdown
## 🌐 Deploy Público

La API está disponible en producción:

- **Base URL:** https://api-tareas.deviaaps.com/api/tareas
- **Health check:** `curl https://api-tareas.deviaaps.com/api/tareas`
```

---

### ⚠️ `fn_persistencia_efectiva` — Add MySQL Service to docker-compose.yml

**Gap:** Production persistence requires MySQL, but `docker-compose.yml` only defines the app container. Running `docker compose up` locally gives H2 (in-memory, data lost on restart).

**Fix:** Add a `mysql` service to `docker-compose.yml` with a named volume, or document explicitly in README that production MySQL is an external GCP Cloud SQL / managed DB:
```yaml
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: ${DB_NAME}
    volumes:
      - mysql_data:/var/lib/mysql
volumes:
  mysql_data:
```

---

### ⚠️ `cq_linter_configurado` — Expand Checkstyle Rules

**Gap:** `checkstyle.xml` only enforces 2 rules (line length + empty statements). Standard Java projects enforce naming, imports, visibility.

**Fix:** Add to `checkstyle.xml`:
```xml
<module name="NeedBraces"/>
<module name="LeftCurly"/>
<module name="WhitespaceAround"/>
<module name="MethodName"/>
<module name="TypeName"/>
<module name="ConstantName"/>
<module name="AvoidStarImport"/>
<module name="UnusedImports"/>
```

---

### ⚠️ `cq_cobertura_alta` — Add Coverage Badge/Link to README

**Gap:** JaCoCo reports 96.6% coverage but this is not visible in the README (report lives in `target/` which is gitignored).

**Fix option A** — GitHub Actions badge (already uploads artifact, just add to README):
```markdown
[![Coverage](https://img.shields.io/badge/coverage-96.6%25-brightgreen)](./)
```

**Fix option B** — Upload report to GitHub Pages or as a commit artifact link in README.

---

### ⚠️ `cq_ci_funcional` — Add CI Status Badge to README

**Gap:** CI pipeline is functional but README has no status badge to verify the last build is green.

**Fix:** Add to README header:
```markdown
[![CI/CD](https://github.com/Jorgeaapaz/MISEIA_1-4-10-api-spring-boot-tareas/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/Jorgeaapaz/MISEIA_1-4-10-api-spring-boot-tareas/actions/workflows/ci-cd.yml)
```

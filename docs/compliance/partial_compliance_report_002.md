# Compliance Report — Partial — 002

**Project:** API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
**Repo:** `MISEIA_1-4-10-api-spring-boot-tareas`  
**Audit Date:** 2026-06-26  
**Prior Report:** `partial_compliance_report_001.md` (24✅/4⚠️/1❌)  
**Result:** ⚠️ PARTIAL COMPLIANCE — 27 ✅ / 2 ⚠️ / 0 ❌ / 1 N/A (out of 30 criteria)

---

## Progress Since Report 001

| Criterion | Report 001 | Report 002 | Change |
|---|---|---|---|
| `fn_deploy_publico_accesible` | ❌ NON-COMPLIANT | ✅ COMPLIANT | README §10.1 added `https://api-tareas.deviaaps.com/api/tareas` |
| `cq_cobertura_alta` | ⚠️ PARTIAL | ✅ COMPLIANT | README §9 now has coverage table (100% dominio, ~97% global) |
| `cq_ci_funcional` | ⚠️ PARTIAL | ✅ COMPLIANT | CI badge confirmed on README line 3 |
| `fn_persistencia_efectiva` | ⚠️ PARTIAL | ⚠️ PARTIAL | No change — `docker-compose.yml` still lacks MySQL service |
| `cq_linter_configurado` | ⚠️ PARTIAL | ⚠️ PARTIAL | No change — `checkstyle.xml` still has only 2 rules |

---

## Summary Score

| Category | ✅ Compliant | ⚠️ Partial | ❌ Non-Compliant | N/A |
|---|---|---|---|---|
| Functionality (10) | 8 | 1 | 0 | 1 |
| Code Quality (10) | 8 | 1 | 0 | 0 |
| Documentation (10) | 10 | 0 | 0 | 0 |
| **Total (30)** | **27** | **2** | **0** | **1** |

---

## Full Compliance Table

### Funcionalidad y cumplimiento del enunciado

| ID | Status | Evidence |
|---|---|---|
| `fn_se_instala` | ✅ | `./mvnw test`, `./mvnw spring-boot:run` in README §5; Maven Wrapper 3.9.6 included — no Maven install required |
| `fn_arranca_local` | ✅ | `./mvnw spring-boot:run` → `http://localhost:8080/api/tareas` documented in README §5 |
| `fn_flujo_principal_funciona` | ✅ | 6 endpoints (GET/POST/PUT/DELETE + filter); 15 tests passing; test 12 verifies full CRUD cycle end-to-end |
| `fn_persistencia_efectiva` | ⚠️ | Dev=H2 in-memory (data lost on restart — documented ADR-001). `docker-compose.yml` has no MySQL service; prod MySQL requires external configuration via env vars. Gap: no default durable storage without manual setup |
| `fn_validaciones_de_entrada` | ✅ | `@NotBlank` + `@Size(max=200/1000)` + `@Valid`; DB constraints mirror Bean Validation; HTTP 400 verified in test 2 |
| `fn_manejo_errores_consistente` | ✅ | 201 (POST), 204 (DELETE), 400 (validation), 404 (not found) — all coded explicitly and tested; no silent 500s |
| `fn_funciones_completas_del_enunciado` | ✅ | Full CRUD + filter by state; all 6 endpoints tested; Postman collection included |
| `fn_features_extra_pertinentes` | ✅ | Filter by state (`GET /estado/{completada}`), audit timestamps (`@PrePersist/@PreUpdate`), `DataSeeder` startup mock data, Postman collection |
| `fn_estados_intermedios_ui` | N/A | Backend API only — no UI in scope |
| `fn_deploy_publico_accesible` | ✅ | README §10.1: `https://api-tareas.deviaaps.com/api/tareas` with `curl` verification command; Traefik TLS wildcard cert |

---

### Calidad de código y arquitectura

| ID | Status | Evidence |
|---|---|---|
| `cq_estructura_carpetas_clara` | ✅ | `controller/service/repository/model/DataSeeder` under `com.tareas.api`; `docs/decisions/compliance/prompts/`; structure documented in README §2 |
| `cq_nombres_descriptivos` | ✅ | `listarTodas()`, `obtenerPorId()`, `fechaCreacion`, `completada` — semantic Spanish camelCase throughout |
| `cq_separacion_responsabilidades` | ✅ | Clean MVC: controller (HTTP) → service (business) → repository (data); constructor injection; no cross-layer imports |
| `cq_dependencias_lockeadas` | ✅ | `pom.xml` Spring Boot BOM 3.2.5; Maven Wrapper 3.9.6; `package-lock.json`; `checkstyle.xml` versioned |
| `cq_tests_minimos` | ✅ | 15 tests (12 integration + 2 seeding + 1 smoke); `./mvnw test`; all passing — documented in README §9 |
| `cq_linter_configurado` | ⚠️ | `checkstyle.xml` committed + Maven plugin v3.3.1 + runs in CI (build fails on violations). **Gap:** only 2 rules (`LineLength:120`, `EmptyStatement`); missing: naming conventions, import ordering, visibility modifiers, braces |
| `cq_sin_secretos_en_repo` | ✅ | `.gitignore` excludes `.env`, `application-prod.properties`; `.env.example` placeholders only; SSH/VM secrets via GitHub Secrets |
| `cq_arquitectura_razonada` | ✅ | Mermaid component diagram README §3.5; 5 ADRs (README §8.3 + `docs/decisions/`); MVC documented §3.1–3.4 |
| `cq_cobertura_alta` | ✅ | Coverage table in README §9: 100% dominio (Controller/Service/Model/DataSeeder), ~97% global; JaCoCo minimum 60% enforced via Maven build; `./mvnw verify` documented |
| `cq_ci_funcional` | ✅ | CI/CD badge on README line 3; GitHub Actions: test→lint→coverage→build→deploy on every push to `master`; PR builds run test+lint+coverage |

---

### Documentación y decisiones

| ID | Status | Evidence |
|---|---|---|
| `dc_readme_presente` | ✅ | 12-section README; §1 endpoints, §2 structure, §3 architecture, §5 quickstart, §6 examples |
| `dc_env_example` | ✅ | `.env.example` with 8 vars (`DB_HOST`, `DB_PORT`, `SPRING_DATASOURCE_*`, `SERVER_PORT`, etc.); placeholders only |
| `dc_comandos_verificacion` | ✅ | `./mvnw test`, `./mvnw verify`, `./mvnw spring-boot:run`, API URL, H2 console — all in README §5 |
| `dc_seccion_uso` | ✅ | README §6: 6 real HTTP examples (create/filter/update/delete/400/404) with request body and status codes |
| `dc_diagrama_arquitectura` | ✅ | Mermaid `graph TD` in README §3.5 (7 nodes: Client→Controller→@Valid→Service→Repository→Entity→H2/MySQL) |
| `dc_decisiones_documentadas` | ✅ | 5 ADRs in README §8.3 + `docs/decisions/` (0001→0003); each with options considered, reasons, consequences |
| `dc_cambios_ia_documentados` | ✅ | README §12: 5 sessions documented (2026-04-20 to 2026-06-26); model (Claude Sonnet 4.6), gaps identified per session |
| `dc_adrs_o_decision_log` | ✅ | `docs/decisions/` (3 ADRs with full format) + README §8.3 (ADR-001→005 with quantitative measurements) |
| `dc_justificacion_cuantitativa` | ✅ | ADR-001: H2=38.9s vs Testcontainers=55-65s (35-40% faster); NFR-PERF-002: startup ~8s measured; README §7.2 fully quantified |
| `dc_instrucciones_deploy` | ✅ | README §10.3: 4 options (Maven/Docker/Docker Compose+Traefik/CI+GCP manual with exact SSH commands) |

---

## Remaining Gaps — Remediation Plan

### ⚠️ `fn_persistencia_efectiva` — Add MySQL Service to docker-compose.yml

**Gap:** The production path requires MySQL but `docker-compose.yml` only defines the application container. The default Spring profile is `dev` (H2 in-memory), so data is lost on restart unless the operator manually configures MySQL externally.

**ADR-001 Context:** This is an intentional trade-off documented in ADR-001 (zero-setup developer experience). However, the evaluation criteria requires data to survive restarts.

**Fix Option A — Add MySQL service to docker-compose.yml:**
```yaml
services:
  mysql:
    image: mysql:8.0
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: ${DB_NAME}
      MYSQL_USER: ${DB_USER}
      MYSQL_PASSWORD: ${DB_PASSWORD}
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - miseia-net

  api-tareas:
    image: api-tareas:latest
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_URL=jdbc:mysql://mysql:3306/${DB_NAME}
      - DB_USER=${DB_USER}
      - DB_PASSWORD=${DB_PASSWORD}
    depends_on:
      - mysql
    # ... rest of existing config

volumes:
  mysql_data:
```

**Fix Option B (current ADR-001 recommendation):** Document explicitly in README §10.3 that production requires MySQL and provide the exact command to set `SPRING_PROFILES_ACTIVE=prod` with the external MySQL URL.

---

### ⚠️ `cq_linter_configurado` — Expand Checkstyle Rules

**Gap:** `checkstyle.xml` enforces only 2 rules. Standard Java style requires naming, import ordering, braces, and visibility rules.

**Current state:**
```xml
<module name="LineLength"><property name="max" value="120"/></module>
<module name="EmptyStatement"/>
```

**Recommended addition to `checkstyle.xml`:**
```xml
<module name="TreeWalker">
    <module name="EmptyStatement"/>
    <module name="NeedBraces"/>
    <module name="LeftCurly"/>
    <module name="AvoidStarImport"/>
    <module name="UnusedImports"/>
    <module name="MethodName"/>
    <module name="TypeName"/>
    <module name="ConstantName"/>
    <module name="LocalVariableName"/>
    <module name="MemberName"/>
    <module name="WhitespaceAround"/>
    <module name="OneStatementPerLine"/>
</module>
```

> **Note:** Run `./mvnw checkstyle:check` after adding rules to identify and fix any new violations before committing.

---

## Path to Full Compliance

To achieve `full_compliance_report.md`, resolve both remaining gaps:

1. **`fn_persistencia_efectiva`** — Add MySQL service to `docker-compose.yml` with named volume (Option A), OR update `docker-compose.yml` to use `SPRING_PROFILES_ACTIVE=prod` with an external MySQL and document it clearly (Option B)
2. **`cq_linter_configurado`** — Expand `checkstyle.xml` to include at least naming, import, and braces rules; fix any new violations; re-run `./mvnw verify`

Both fixes are low-risk, isolated changes with no impact on existing tests or business logic.

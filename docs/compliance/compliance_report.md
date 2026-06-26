# Compliance Report — 1-4-10-api-spring-boot-tareas
**Date:** 2026-06-25  
**Evaluator:** MISEIA-JAAP-Student (claude-sonnet-4-6)  
**Project:** API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
**Repo:** `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`

> **Skipped items (per project arguments):** `fn_persistencia_efectiva`, `fn_estados_intermedios_ui`

---

## 1. Funcionalidad y cumplimiento del enunciado

| ID | Requirement | Status | Notes |
|----|-------------|--------|-------|
| `fn_se_instala` | Instrucciones de instalación sin errores | ✅ PASS | README documents `./mvnw test` and `./mvnw spring-boot:run`; Maven wrapper included |
| `fn_arranca_local` | App arranca con comando documentado | ✅ PASS | `./mvnw spring-boot:run` → `http://localhost:8080/api/tareas` |
| `fn_flujo_principal_funciona` | CRUD end-to-end sin errores | ✅ PASS | 13/13 integration tests passing; full CRUD verified |
| `fn_persistencia_efectiva` | Datos sobreviven reinicio | ⏭ SKIPPED | Per project arguments |
| `fn_validaciones_de_entrada` | Inputs validados, 400/422 en error | ✅ PASS | `@NotBlank`, `@Size` on Tarea; test verifies 400 on blank titulo |
| `fn_manejo_errores_consistente` | Errores controlados con status + cuerpo | ✅ PASS | 404 for not found, 204 for delete, 400 for validation |
| `fn_funciones_completas_del_enunciado` | Todas las funcionalidades implementadas | ✅ PASS | Full CRUD + filter by status (`/estado/{completada}`) |
| `fn_features_extra_pertinentes` | Features extra pertinentes | ✅ PASS | `GET /api/tareas/estado/{completada}` filter; Postman collection |
| `fn_estados_intermedios_ui` | UI maneja estados de carga/error | ⏭ SKIPPED | Per project arguments (no UI) |
| `fn_deploy_publico_accesible` | Deploy público accesible con URL en README | ❌ FAIL | No public URL; only local dev documented |

**Section score: 7/8 applicable items passing**

---

## 2. Calidad de código y arquitectura

| ID | Requirement | Status | Notes |
|----|-------------|--------|-------|
| `cq_estructura_carpetas_clara` | Estructura de carpetas refleja arquitectura | ✅ PASS | `controller/`, `service/`, `repository/`, `model/` clearly separated |
| `cq_nombres_descriptivos` | Nombres descriptivos | ✅ PASS | TareaController, TareaService, TareaRepository, Tarea — all clear |
| `cq_separacion_responsabilidades` | Capas separadas | ✅ PASS | Controller → Service → Repository → Entity; constructor injection used |
| `cq_dependencias_lockeadas` | Lockfile presente | ✅ PASS | `pom.xml` with Spring Boot parent 3.2.5 (fixed); Maven wrapper properties pinned |
| `cq_tests_minimos` | Tests automatizados cubren flujos críticos | ✅ PASS | 13 integration tests in `TareaIntegrationTest`; covers all endpoints + edge cases |
| `cq_linter_configurado` | Linter/formatter configurado y versionado | ❌ FAIL | No Checkstyle, Spotless, or any formatter plugin in `pom.xml` |
| `cq_sin_secretos_en_repo` | Sin credenciales en repo | ✅ PASS | `.gitignore` excludes `.env`, `application-prod.properties`; H2 has no real password |
| `cq_arquitectura_razonada` | Arquitectura por capas explícita | ✅ PASS | MVC layered, constructor DI, Repository Pattern, `@PrePersist`/`@PreUpdate` lifecycle |
| `cq_cobertura_alta` | Cobertura >60% reportada | ❌ FAIL | No JaCoCo or coverage plugin; no report generated or badged |
| `cq_ci_funcional` | Pipeline CI pasa tests + linter en cada push | ❌ FAIL | `.gitlab-ci.yml` runs `mvn clean package -DskipTests` — **tests are skipped**; no GitHub Actions |

**Section score: 7/10 items passing**

---

## 3. Documentación y decisiones

| ID | Requirement | Status | Notes |
|----|-------------|--------|-------|
| `dc_readme_presente` | README con qué hace, instalación, endpoints | ✅ PASS | Comprehensive README with endpoints, structure, design patterns, and getting-started |
| `dc_env_example` | `.env.example` con variables requeridas | ❌ FAIL | No `.env.example` in project root (only in `004_Infra_in_VM`) |
| `dc_comandos_verificacion` | README incluye comandos exactos de verificación | ✅ PASS | `./mvnw test`, `./mvnw spring-boot:run`, exact URLs documented |
| `dc_seccion_uso` | Ejemplo de uso real (request/response) | ✅ PASS | "Example Output" section with HTTP request/response examples |
| `dc_diagrama_arquitectura` | Diagrama de arquitectura | ❌ FAIL | Only text description; no ASCII/Mermaid/diagram |
| `dc_decisiones_documentadas` | Decisiones con trade-offs reales | ❌ FAIL | "Design Patterns" section describes what was done, not WHY vs. alternatives |
| `dc_cambios_ia_documentados` | Cambios respecto al borrador de IA documentados | ❌ FAIL | No section documenting AI-assisted vs. human-authored changes |
| `dc_adrs_o_decision_log` | ADRs o decision log estructurado | ❌ FAIL | No ADRs or decision log |
| `dc_justificacion_cuantitativa` | Decisión técnica justificada con números | ❌ FAIL | No benchmarks, latency measurements, or cost comparisons |
| `dc_instrucciones_deploy` | Instrucciones de despliegue verificables | ❌ FAIL | Only local dev; no Dockerfile, no cloud deploy section |

**Section score: 4/10 items passing**

---

## Summary

| Section | Passing | Total Applicable | % |
|---------|---------|-----------------|---|
| Funcionalidad | 7 | 8 | 87.5% |
| Calidad de código | 7 | 10 | 70% |
| Documentación | 4 | 10 | 40% |
| **Overall** | **18** | **28** | **64.3%** |

---

## Non-Compliant Items (Action Required)

| # | Item | Priority | Prompt File |
|---|------|----------|-------------|
| 1 | `dc_env_example` | High | `001_env_example_config_fn_prompt.md` |
| 2 | `cq_linter_configurado` | High | `002_linter_checkstyle_config_fn_prompt.md` |
| 3 | `cq_cobertura_alta` | High | `003_coverage_jacoco_report_fn_prompt.md` |
| 4 | `dc_diagrama_arquitectura` | Medium | `004_arch_diagram_readme_fn_prompt.md` |
| 5 | `dc_decisiones_documentadas` | Medium | `005_decisions_tradeoffs_readme_fn_prompt.md` |
| 6 | `dc_cambios_ia_documentados` | Medium | `006_ai_changes_documented_fn_prompt.md` |
| 7 | `dc_adrs_o_decision_log` | Medium | `007_adrs_decision_log_fn_prompt.md` |
| 8 | `dc_justificacion_cuantitativa` | Low | `008_justification_quantitative_fn_prompt.md` |
| 9 | `dc_instrucciones_deploy` | High | `009_deploy_instructions_readme_fn_prompt.md` |
| 10 | `cq_ci_funcional` (GitLab) | High | `010_ci_pipeline_gitlab_fn_prompt.md` |
| 11 | `cq_ci_funcional` + `fn_deploy_publico_accesible` (GitHub) | High | `011_ci_pipeline_github_fn_prompt.md` |

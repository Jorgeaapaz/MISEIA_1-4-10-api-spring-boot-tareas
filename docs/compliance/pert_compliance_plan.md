# PERT Compliance Plan — 1-4-10-api-spring-boot-tareas
**Date:** 2026-06-25  
**Project:** API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17

---

## PERT Compliance Plan

The following tasks are ordered by logical dependency (PERT List). Tasks with no predecessors can start immediately in parallel; tasks with predecessors must wait for their dependencies to complete.

### Node Definitions

| Node | Item | Prompt File | Predecessors |
|------|------|-------------|--------------|
| A | `dc_env_example` — Add `.env.example` | `001_env_example_config_fn_prompt.md` | — (start) |
| B | `cq_linter_configurado` — Configure Checkstyle | `002_linter_checkstyle_config_fn_prompt.md` | — (start) |
| C | `cq_cobertura_alta` — Add JaCoCo coverage | `003_coverage_jacoco_report_fn_prompt.md` | — (start) |
| D | `dc_diagrama_arquitectura` — Add architecture diagram | `004_arch_diagram_readme_fn_prompt.md` | — (start) |
| E | `dc_decisiones_documentadas` — Document trade-offs | `005_decisions_tradeoffs_readme_fn_prompt.md` | — (start) |
| F | `dc_cambios_ia_documentados` — Document AI changes | `006_ai_changes_documented_fn_prompt.md` | — (start) |
| G | `dc_adrs_o_decision_log` — Write ADRs | `007_adrs_decision_log_fn_prompt.md` | E |
| H | `dc_justificacion_cuantitativa` — Quantitative justification | `008_justification_quantitative_fn_prompt.md` | G |
| I | `cq_ci_funcional` GitLab — Fix GitLab CI pipeline | `010_ci_pipeline_gitlab_fn_prompt.md` | B, C |
| J | `cq_ci_funcional` + `fn_deploy_publico_accesible` GitHub — GitHub Actions + Deploy | `011_ci_pipeline_github_fn_prompt.md` | A, B, C |
| K | `dc_instrucciones_deploy` — Add deploy section to README | `009_deploy_instructions_readme_fn_prompt.md` | J |

### PERT Dependency Graph

```
START ──┬── A ──────────────────────────────────┬── J ── K
        ├── B ─────────────────────────┬── I    │
        ├── C ─────────────────────────┘         │ (A,B,C required)
        ├── D (independent)                       │
        ├── E ── G ── H                           │
        └── F (independent)
```

**Critical Path:** A → J → K  *(longest path: env_example → GitHub CI+Deploy → README deploy instructions)*

**Parallel fast tracks (no deps):**
- B + C can run in parallel with A
- D and F can run at any time independently
- E → G → H is a standalone documentation chain

---

## Proper Execution of Tasks

Execute tasks in the following order. Tasks at the same level can run in parallel.

**Level 1 — Independent (start immediately, can run in parallel):**
1. `A` — Create `.env.example` → `001_env_example_config_fn_prompt.md`
2. `B` — Configure Checkstyle linter → `002_linter_checkstyle_config_fn_prompt.md`
3. `C` — Add JaCoCo coverage reporting → `003_coverage_jacoco_report_fn_prompt.md`
4. `D` — Add architecture diagram to README → `004_arch_diagram_readme_fn_prompt.md`
5. `E` — Document trade-off decisions in README → `005_decisions_tradeoffs_readme_fn_prompt.md`
6. `F` — Document AI-assisted changes → `006_ai_changes_documented_fn_prompt.md`

**Level 2 — Requires Level 1 predecessors:**
7. `G` — Write ADRs (after `E`) → `007_adrs_decision_log_fn_prompt.md`
8. `I` — Fix GitLab CI pipeline (after `B` and `C`) → `010_ci_pipeline_gitlab_fn_prompt.md`
9. `J` — GitHub Actions CI + Deploy to VM (after `A`, `B`, `C`) → `011_ci_pipeline_github_fn_prompt.md`

**Level 3 — Requires Level 2 predecessors:**
10. `H` — Quantitative justification (after `G`) → `008_justification_quantitative_fn_prompt.md`
11. `K` — Add deploy instructions to README (after `J`) → `009_deploy_instructions_readme_fn_prompt.md`

---

## Notes

- The GitHub Actions task (`J`) is **favored over the GitLab CI task** (`I`) in the critical path, per evaluation rules.
- Tasks `D`, `E`, `F`, `G`, `H` are documentation-only and do not block code changes.
- All CI/CD tasks (`I`, `J`) depend on linter (`B`) and coverage (`C`) being configured first so the pipeline can validate them.
- After completing task `J`, the public URL must be added to `README.md` to satisfy `fn_deploy_publico_accesible`.

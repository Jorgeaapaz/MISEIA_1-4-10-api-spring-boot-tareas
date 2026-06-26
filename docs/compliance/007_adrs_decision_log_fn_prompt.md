@~/.claude/prompts/new_functionality_prompt_spec.md

# Write Architecture Decision Records (ADRs)

## Role
Act as a Software Architect who writes structured Architecture Decision Records following the MADR (Markdown Any Decision Records) format.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
Predecessor: `005_decisions_tradeoffs_readme_fn_prompt.md` (must be done first — trade-offs must be documented in README before writing ADRs)

**Non-compliant item:** `dc_adrs_o_decision_log`  
ADRs are structured records with Context / Decision / Consequences format, stored in `docs/decisions/` or `docs/adr/`. They are more formal than the "## Decisions" section in README and provide a persistent, auditable log of architectural choices.

## Task
Create at least 3 ADR files in `docs/decisions/` directory following the MADR format. Use the same decisions identified in `005_decisions_tradeoffs_readme_fn_prompt.md` but expand them with full MADR structure.

### ADR Guidelines
- Directory: `docs/decisions/` (create if not exists)
- Naming: `0001-use-h2-for-dev-and-mysql-for-production.md`, `0002-spring-data-jpa-derived-queries.md`, `0003-integration-tests-only-no-unit-mocks.md`
- Format: MADR (Markdown Architectural Decision Record)
- Status: `Accepted` for all current decisions
- Include: Context, Decision Drivers, Considered Options, Decision Outcome, Pros/Cons of chosen option

### MADR Template
```markdown
# [ADR-NUMBER]: [Short Title]

**Date:** YYYY-MM-DD  
**Status:** Accepted | Proposed | Deprecated | Superseded  
**Deciders:** Jorge Aguilar  

## Context and Problem Statement
What problem or requirement led to this decision?

## Decision Drivers
- Driver 1 (constraint, requirement, or quality attribute)
- Driver 2

## Considered Options
- Option A: [Name]
- Option B: [Name]

## Decision Outcome
Chosen option: **Option A**, because [reason].

### Positive Consequences
- ...

### Negative Consequences
- ...

## Pros and Cons of the Options

### Option A: [Name]
- Good: [argument]
- Bad: [argument]

### Option B: [Name]
- Good: [argument]
- Bad: [argument]
```

## Output Format
1. `docs/decisions/0001-h2-dev-mysql-production.md`
2. `docs/decisions/0002-spring-data-derived-queries.md`
3. `docs/decisions/0003-integration-tests-only.md`
4. `docs/decisions/README.md` — index listing all ADRs

## Examples and Steps to Follow
1. Read source files to confirm factual details for each ADR
2. Write ADR-0001 for H2 vs MySQL decision
3. Write ADR-0002 for Spring Data derived queries vs `@Query`
4. Write ADR-0003 for integration tests only vs unit + integration
5. Create `docs/decisions/README.md` as index
6. Commit all files

## Output Checklist and Guardrails
- [ ] 3+ ADR files created in `docs/decisions/`
- [ ] Each ADR follows MADR format with all required sections
- [ ] Status is `Accepted` for all
- [ ] Date is accurate (session date: 2026-04-20)
- [ ] Index file `docs/decisions/README.md` lists all ADRs
- [ ] No factual inaccuracies (verify against actual source code)
- [ ] Commit with message referencing ADR addition

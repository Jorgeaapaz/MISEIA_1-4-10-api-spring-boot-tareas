@~/.claude/prompts/new_functionality_prompt_spec.md

# Document Architecture Decisions and Trade-offs in README

## Role
Act as a Software Architect who writes clear, opinionated technical documentation explaining real design trade-offs.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`

**Non-compliant item:** `dc_decisiones_documentadas`  
The README has a "Design Patterns / Architecture" section describing WHAT patterns were used (MVC, Repository, Constructor DI, JPA Lifecycle Callbacks), but it does not explain WHY those choices were made over alternatives, or what trade-offs were accepted. Evaluation requires at least 2 real trade-offs documented.

Specific decisions made in this project that have real alternatives:
1. H2 in-memory (dev) vs. MySQL (prod) — why not just one DB for both?
2. Spring Data JPA (findByCompletada) vs. custom @Query — why use derived query methods?
3. Integration tests only vs. unit tests + integration tests — why no mocked unit tests?
4. `application.properties` (single file) vs. Spring profiles (`application-dev.properties`, `application-prod.properties`) — why single file?
5. No global exception handler (`@ControllerAdvice`) — why accept default Spring error response?

## Task
Add a "## Decisions" section to `README.md` documenting at least 3 real trade-offs from the project. Each decision must follow this structure:
- **Decision**: What was chosen
- **Alternatives considered**: What was NOT chosen
- **Reason**: WHY this choice was made (technical argument, not "it's popular")
- **Trade-off accepted**: What was sacrificed

### Documentation Guidelines
- Write from the perspective of the developer who made the choice
- Be specific — avoid generic statements like "Spring Boot is easy to use"
- Reference actual code artifacts (class names, annotations, config keys)
- Keep each decision under 100 words
- Place the section after "## Design Patterns / Architecture" and before "## Getting Started"

## Output Format
1. Updated `README.md` with a new "## Decisions" section containing 3+ documented trade-offs

## Examples and Steps to Follow
1. Read the current README and all source files to identify real decisions
2. Select 3 decisions with the most interesting trade-offs
3. Write each decision with the 4-field format
4. Insert section into README at the appropriate location

Example decision entry:
```markdown
### Decision 1: H2 in-memory for dev/test, MySQL for production

**Decision:** Use H2 in `application.properties` (default profile) and MySQL via `mysql-connector-j` for production.

**Alternatives considered:** Use a single MySQL instance for both development and testing (Testcontainers or shared local MySQL).

**Reason:** H2 requires zero setup — any developer can clone and run `./mvnw test` without installing a database server. Integration tests use `@SpringBootTest` with in-memory H2, which is deterministic and fast (~38s for 13 tests).

**Trade-off accepted:** Tests pass against H2 but not against MySQL, so SQL dialect differences could hide bugs. Accepted because the query surface is small (only `findAll`, `findById`, `findByCompletada`, `save`, `deleteById`) — none use DB-specific syntax.
```

## Output Checklist and Guardrails
- [ ] At least 3 decisions documented
- [ ] Each decision follows Decision / Alternatives / Reason / Trade-off format
- [ ] No generic statements — each reason is specific to this project
- [ ] README section title is "## Decisions" or "## Decisiones de Diseño"
- [ ] Existing README content preserved
- [ ] Commit with descriptive message

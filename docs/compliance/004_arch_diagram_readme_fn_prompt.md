@~/.claude/prompts/new_functionality_prompt_spec.md

# Add Architecture Diagram to README

## Role
Act as a Software Architect with expertise in documenting Spring Boot layered architectures using Mermaid or ASCII diagrams.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
Architecture: Layered MVC — Controller → Service → Repository → Entity (Tarea)

**Non-compliant item:** `dc_diagrama_arquitectura`  
The README has a text-based "Design Patterns / Architecture" section that describes the layers, but has no visual diagram. Evaluation requires a diagram (ASCII, Mermaid, or draw.io) showing components and main flows.

## Task
Add a Mermaid architecture diagram to `README.md` that shows:
1. The layered component structure (Controller → Service → Repository → DB)
2. The HTTP request/response flow for a typical CRUD operation
3. The technology stack at each layer

### Diagram Guidelines
- Use Mermaid (`graph TD` or `sequenceDiagram`) — GitHub renders Mermaid natively
- Show: HTTP Client → TareaController → TareaService → TareaRepository → H2/MySQL
- Include Bean Validation (`@Valid`) as a cross-cutting concern
- Include Spring Boot auto-configuration annotations at each layer
- Add a second diagram showing the sequence of a POST /api/tareas request (optional but valuable)
- Place diagram in a "## Architecture" section ABOVE "## Getting Started" in the README

### README Placement
- Insert new section "## Architecture" after "## Design Patterns / Architecture" (or replace it)
- Keep existing text description if it adds value; diagram goes first

## Output Format
1. Updated `README.md` with Mermaid diagram(s) in a new "## Architecture" section
2. Verify diagram renders correctly in GitHub's Mermaid preview

## Examples and Steps to Follow
1. Read current `README.md` to understand existing structure
2. Design a `graph TD` diagram for the component layers
3. Design a `sequenceDiagram` for a POST /api/tareas flow
4. Insert both diagrams into README under a new "## Architecture" section
5. Preview the Mermaid syntax for correctness

Example component diagram:
```mermaid
graph TD
    Client[HTTP Client<br/>curl / Postman] -->|HTTP Request| Controller

    subgraph Spring Boot Application
        Controller["TareaController<br/>@RestController<br/>/api/tareas"]
        Validation["Bean Validation<br/>@Valid / @NotBlank / @Size"]
        Service["TareaService<br/>@Service<br/>Business Logic"]
        Repository["TareaRepository<br/>@Repository<br/>JpaRepository"]
    end

    Controller -->|@Valid| Validation
    Controller --> Service
    Service --> Repository
    Repository -->|JPA / Hibernate| DB[(H2 in-memory<br/>dev / test)]
    Repository -.->|MySQL connector<br/>prod profile| MySQL[(MySQL<br/>production)]

    Controller -->|ResponseEntity| Client
```

## Output Checklist and Guardrails
- [ ] Mermaid diagram renders without syntax errors
- [ ] Diagram shows all 4 layers (Controller, Service, Repository, DB)
- [ ] Technology stack annotated at each node
- [ ] H2 vs MySQL distinction shown
- [ ] README section title is "## Architecture" or "## Arquitectura"
- [ ] Existing README content preserved (only adding, not removing sections)
- [ ] Commit with descriptive message

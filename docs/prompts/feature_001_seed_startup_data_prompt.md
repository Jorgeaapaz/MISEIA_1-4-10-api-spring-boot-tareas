@~/.claude/prompts/new_functionality_prompt_spec.md

# Feature: Seed Mock Task Data on Application Startup (Dev & Prod)

## Role
Act as a Java Spring Boot Developer and Software Architect. You are an expert in Spring Boot lifecycle management, Spring Profiles, JPA, and application data initialization strategies.

## Context

**Project:** `api-tareas` — Spring Boot REST API for task management  
**Repository:** `d:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
**Language:** Java 17+, Spring Boot 3.x  
**Database (dev):** H2 in-memory (`jdbc:h2:mem:tareasdb`)  
**Database (prod):** MySQL via Docker (see `docker-compose.yml`)  
**Entity:** `com.tareas.api.model.Tarea` — fields: `id`, `titulo`, `descripcion`, `completada`, `fechaCreacion`, `fechaActualizacion`

**Current state:**
- `schema.sql` only contains `CREATE TABLE IF NOT EXISTS tareas` — no seed data
- `application.properties` uses `spring.jpa.hibernate.ddl-auto=update` — no data initialization
- No `CommandLineRunner`, `ApplicationRunner`, or `@PostConstruct` data loader exists
- No Spring profiles are defined (`application-dev.properties` / `application-prod.properties` are absent)
- The app starts with an **empty database** in all environments

## Task

Implement a `DataSeeder` component that inserts a predefined set of mock `Tarea` records at application startup, for **both dev and prod profiles**.

### Implementation Guidelines

1. **Create `DataSeeder.java`** at `src/main/java/com/tareas/api/DataSeeder.java`:
   - Implement `CommandLineRunner`
   - Annotate with `@Component`
   - Inject `TareaRepository` via constructor injection
   - Check if the table is empty (`repository.count() == 0`) before inserting, to ensure **idempotency** — no duplicate inserts on restart
   - Insert at least **5 meaningful mock tasks** using the two-arg constructor `new Tarea(titulo, descripcion)`
   - Mark some tasks as `completada = true` to have varied test data
   - Log seed activity with `org.slf4j.Logger` (`LoggerFactory.getLogger`)

2. **Seed data requirements** — include representative variety:
   ```
   - "Configurar entorno de desarrollo"     completada=true
   - "Implementar endpoint GET /tareas"     completada=true  
   - "Agregar validaciones al modelo"       completada=false
   - "Escribir pruebas de integración"      completada=false
   - "Documentar la API con Postman"        completada=false
   ```

3. **Profile configuration:**
   - Create `src/main/resources/application-dev.properties`:
     ```properties
     spring.datasource.url=jdbc:h2:mem:tareasdb
     spring.datasource.driver-class-name=org.h2.Driver
     spring.datasource.username=sa
     spring.datasource.password=
     spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
     spring.jpa.hibernate.ddl-auto=create-drop
     spring.h2.console.enabled=true
     spring.h2.console.path=/h2-console
     ```
   - Create `src/main/resources/application-prod.properties`:
     ```properties
     spring.datasource.url=${DB_URL}
     spring.datasource.username=${DB_USER}
     spring.datasource.password=${DB_PASSWORD}
     spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
     spring.jpa.hibernate.ddl-auto=update
     spring.h2.console.enabled=false
     ```
   - Update base `application.properties` to set `spring.profiles.active=dev` as default so local dev works without extra config

4. **Checkstyle compliance:** The project uses `checkstyle.xml`. Ensure:
   - All classes have proper Javadoc header
   - No wildcard imports
   - Line length ≤ 120 characters
   - Proper spacing and braces per existing code style

5. **Test coverage:** Update or add a test in `src/test/java/com/tareas/api/TareaIntegrationTest.java`:
   - Add a test that verifies the database is **not empty** after application context loads
   - Assert that at least 5 records exist
   - Assert that at least one record has `completada = true`

### Seed Data Safety Rules

- **Never** truncate or delete existing data before seeding — only seed when `count() == 0`
- **Never** hardcode primary key IDs — let `AUTO_INCREMENT` / `IDENTITY` handle assignment
- The `fechaCreacion` and `fechaActualizacion` fields are managed by `@PrePersist` / `@PreUpdate` in the entity — do not set them manually

## Output Format

Produce the following files:

```
src/
  main/
    java/com/tareas/api/
      DataSeeder.java                      ← new file
    resources/
      application.properties              ← updated (add spring.profiles.active=dev)
      application-dev.properties          ← new file
      application-prod.properties         ← new file
  test/
    java/com/tareas/api/
      TareaIntegrationTest.java           ← updated (add seeding assertion tests)
```

## Examples and Steps to Follow

### Step 1 — Create `DataSeeder.java`

```java
package com.tareas.api;

import com.tareas.api.model.Tarea;
import com.tareas.api.repository.TareaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the database with initial mock task data on application startup.
 * Runs for all profiles (dev and prod). Insertion is idempotent.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final TareaRepository tareaRepository;

    public DataSeeder(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public void run(String... args) {
        if (tareaRepository.count() == 0) {
            logger.info("Seeding initial task data...");

            Tarea t1 = new Tarea("Configurar entorno de desarrollo",
                    "Instalar JDK, Maven, Docker y configurar variables de entorno");
            t1.setCompletada(true);

            Tarea t2 = new Tarea("Implementar endpoint GET /tareas",
                    "Crear controller, service y repository para listar todas las tareas");
            t2.setCompletada(true);

            Tarea t3 = new Tarea("Agregar validaciones al modelo",
                    "Usar @NotBlank y @Size en los campos del modelo Tarea");

            Tarea t4 = new Tarea("Escribir pruebas de integración",
                    "Cubrir endpoints CRUD con MockMvc o TestRestTemplate");

            Tarea t5 = new Tarea("Documentar la API con Postman",
                    "Exportar colección Postman con todos los endpoints y ejemplos");

            tareaRepository.saveAll(List.of(t1, t2, t3, t4, t5));
            logger.info("Seeded {} tasks successfully.", tareaRepository.count());
        } else {
            logger.info("Database already contains data. Skipping seed.");
        }
    }
}
```

### Step 2 — Update `application.properties`

Add at the top:
```properties
spring.profiles.active=dev
```

### Step 3 — Integration test assertion

```java
@Test
void databaseShouldBeSeededOnStartup() {
    long count = tareaRepository.count();
    assertThat(count).isGreaterThanOrEqualTo(5);
}

@Test
void atLeastOneTaskShouldBeCompleted() {
    List<Tarea> completed = tareaRepository.findAll()
        .stream()
        .filter(Tarea::isCompletada)
        .toList();
    assertThat(completed).isNotEmpty();
}
```

## Output Checklist and Guardrails

- [ ] `DataSeeder` implements `CommandLineRunner` and is annotated with `@Component`
- [ ] Constructor injection used (no `@Autowired` on fields)
- [ ] Guard clause `count() == 0` prevents duplicate seeding on restart
- [ ] Timestamps not set manually — rely on `@PrePersist` in `Tarea`
- [ ] No hardcoded IDs in seed data
- [ ] `application-dev.properties` uses H2; `application-prod.properties` uses `${DB_URL}` env var
- [ ] Base `application.properties` sets `spring.profiles.active=dev` as default
- [ ] Checkstyle passes (`./mvnw checkstyle:check`)
- [ ] All existing tests still pass (`./mvnw test`)
- [ ] New integration tests assert seeded data exists
- [ ] `TareaRepository` has a `setCompletada` setter on the `Tarea` model (add if missing)
- [ ] Logger messages are informative and use parameterized format (no string concatenation)

@~/.claude/prompts/new_functionality_prompt_spec.md

# Add JaCoCo Code Coverage Reporting

## Role
Act as a Java Software Engineer and build tool expert, specializing in Maven plugins and test coverage metrics.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
Build tool: Maven 3.9.6 (via wrapper `./mvnw`)  
Tests: 13 integration tests in `TareaIntegrationTest.java` + 1 context load test in `ApiTareasApplicationTests.java`

**Non-compliant item:** `cq_cobertura_alta`  
The project has excellent integration test coverage (all endpoints + edge cases) but no JaCoCo plugin configured. Evaluation requires coverage >60% lines in domain code, >40% global, with a report attached to README or via badge.

## Task
Add JaCoCo Maven plugin to `pom.xml` to:
1. Generate a coverage report during `mvn verify`
2. Enforce minimum thresholds (>60% line coverage on domain code, >40% global)
3. Generate an HTML report in `target/site/jacoco/`
4. Add a coverage badge or coverage summary to `README.md`

### JaCoCo Plugin Guidelines
- Plugin: `jacoco-maven-plugin` version `0.8.12` or higher
- Bind `prepare-agent` to `initialize` phase (before tests run)
- Bind `report` to `verify` phase (after tests complete)
- Add a coverage threshold check (`check` goal) with:
  - `BUNDLE` level minimum: 60% LINE coverage
  - Exclude generated code and configuration classes (`ApiTareasApplication.java`)
- Exclude from coverage: `**/ApiTareasApplication.class` (main entry point, not domain logic)

### README Update
- Add a "Test Coverage" section with the command to generate the report: `./mvnw verify`
- Mention report location: `target/site/jacoco/index.html`
- Add a static badge (Shields.io or plain text) showing approximate coverage percentage

## Output Format
1. Updated `pom.xml` with `jacoco-maven-plugin` configured
2. Updated `README.md` with coverage section and instructions
3. Confirm `./mvnw verify` generates report at `target/site/jacoco/index.html`

## Examples and Steps to Follow
1. Read current `pom.xml` to understand existing plugins
2. Add JaCoCo plugin with prepare-agent + report + check goals
3. Run `./mvnw verify` to generate the first coverage report
4. Open `target/site/jacoco/index.html` and note the coverage %
5. Update README with the actual measured coverage %
6. Tune thresholds to match measured coverage (or fix gaps if coverage is low)

Example plugin block:
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
    <executions>
        <execution>
            <id>prepare-agent</id>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>verify</phase>
            <goals><goal>report</goal></goals>
        </execution>
        <execution>
            <id>check</id>
            <goals><goal>check</goal></goals>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.60</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
                <excludes>
                    <exclude>**/ApiTareasApplication.class</exclude>
                </excludes>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Output Checklist and Guardrails
- [ ] `jacoco-maven-plugin` present in `pom.xml` with pinned version
- [ ] `./mvnw verify` generates `target/site/jacoco/index.html`
- [ ] Coverage thresholds configured (60% line minimum)
- [ ] Build fails if coverage drops below threshold
- [ ] README updated with coverage section
- [ ] Commit all changes with a descriptive message

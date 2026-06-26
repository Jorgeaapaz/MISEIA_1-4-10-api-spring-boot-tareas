@~/.claude/prompts/new_functionality_prompt_spec.md

# Add Quantitative Justification for Technical Decision

## Role
Act as a Software Engineer who validates technical decisions with measurable data — benchmarks, latency measurements, test timing, or cost comparisons.

## Context
Project: API REST de Gestión de Tareas — Spring Boot 3.2.5 / Java 17  
Location: `D:\Master-IA-Dev\04-Bloque4\1-4-10-api-spring-boot-tareas`  
Predecessor: `007_adrs_decision_log_fn_prompt.md` (ADRs must exist before adding quantitative data to one)

**Non-compliant item:** `dc_justificacion_cuantitativa`  
Evaluation requires at least one technical decision to be backed by numbers (benchmark, latency measured, cost comparison, or comparison with alternative). The ADRs created in task 007 have qualitative reasoning but no measured data.

The best candidate is **ADR-0003: Integration Tests Only** — the 13 integration tests run in ~38 seconds (documented in `RETROSPECTIVA-2026-04-20.md`). This can be compared to a hypothetical unit test setup with mocks.

Alternatively, **ADR-0001: H2 vs MySQL** can be quantified by measuring the test startup time with H2 and estimating what a Testcontainers MySQL setup would cost (startup time typically 10-30s extra per test run).

## Task
Add quantitative data to at least one existing ADR in `docs/decisions/` and create a benchmark/measurement note. The quantification must:
1. Reference a real measured number (from the session retrospective or a new measurement)
2. Compare it to an alternative (estimated or measured)
3. Conclude with a data-backed statement

### Measurement Guidelines
- Use the existing measurement from `RETROSPECTIVA-2026-04-20.md`: "13/13 tests passed in 38.9 seconds"
- Run `./mvnw test` to get a fresh timing measurement
- Calculate per-test average: 38.9s / 13 tests = ~3s per integration test
- Compare to typical Testcontainers MySQL startup: ~15-25s additional overhead per test suite run
- Or: compare H2 schema creation time vs MySQL initialization time

### Files to Update
- Update `docs/decisions/0003-integration-tests-only.md` OR `docs/decisions/0001-h2-dev-mysql-production.md`
- Add a "## Measurements" section with the data
- Optionally add a badge or note in README pointing to the ADR with measured data

## Output Format
1. Updated ADR file with new "## Measurements" section
2. Optionally updated README pointing to the measured ADR

## Examples and Steps to Follow
1. Run `./mvnw test` and note the exact timing
2. Research Testcontainers MySQL startup overhead (or use known typical value)
3. Write the "## Measurements" section with the comparison table
4. Commit the updated ADR

Example measurements section:
```markdown
## Measurements

Test suite timing (measured 2026-04-20, Java 17, Maven 3.9.6, Windows 11):

| Setup | Suite Run Time | Notes |
|-------|---------------|-------|
| H2 in-memory (current) | **38.9 seconds** | 13 tests, `@SpringBootTest(RANDOM_PORT)` |
| MySQL via Testcontainers (estimated) | ~55–65 seconds | +15–25s container startup + pull time on cold start |

**Conclusion:** H2 saves approximately 40% of test suite run time vs a containerized MySQL approach. For a 13-test suite this is a 16–26 second saving per CI run. The trade-off (H2 dialect differences) is acceptable given the query surface is limited to 5 standard JPA methods with no DB-specific SQL.
```

## Output Checklist and Guardrails
- [ ] At least one real measured number included (run the tests to get it)
- [ ] Comparison with at least one alternative value
- [ ] Data-backed conclusion sentence
- [ ] Numbers are accurate — do not fabricate measurements
- [ ] Source of comparison values cited (documentation, personal measurement, or "typical" with caveat)
- [ ] ADR file updated without breaking existing MADR format
- [ ] Commit with descriptive message

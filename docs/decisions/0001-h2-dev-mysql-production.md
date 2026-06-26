# ADR-0001: H2 in-memory for dev/test, MySQL connector for production

**Date:** 2026-04-20  
**Status:** Accepted  
**Deciders:** Jorge Aguilar

---

## Context and Problem Statement

The API needs a relational database to persist tasks. The development workflow must allow any contributor to clone the repo and run tests without installing external services. The production deployment requires a durable database that survives restarts.

## Decision Drivers

- Zero-setup developer experience: `git clone` → `./mvnw test` must work without any DB installation
- Production data durability: tasks must survive application restarts
- CI pipeline must not require external services (no Docker, no cloud DB in the build environment)
- Spring Boot 3.2.5 supports both H2 and MySQL out of the box

## Considered Options

- **Option A:** H2 in-memory for dev/test, MySQL for production (chosen)
- **Option B:** MySQL for all environments (Testcontainers for CI/tests)
- **Option C:** H2 file-based for all environments (durability without MySQL)

## Decision Outcome

Chosen option: **Option A**, because it gives the fastest zero-config developer experience while providing a clear path to production durability via Spring profiles.

### Positive Consequences

- Developers can run all 13 integration tests without installing MySQL
- Test suite completes in ~40 seconds
- CI pipeline (GitHub Actions / GitLab CI) needs only a JVM — no DB service
- MySQL is available for production by switching `SPRING_PROFILES_ACTIVE=prod`

### Negative Consequences

- Tests run against H2 dialect, not MySQL — SQL dialect bugs could be hidden
- H2 in-memory data is lost on every test run and every app restart (by design for tests)
- Switching profiles between dev and prod requires discipline

## Pros and Cons of the Options

### Option A: H2 in-memory (dev/test) + MySQL (prod)
- Good: Zero setup for contributors
- Good: Tests are fast and deterministic (no shared state between runs)
- Bad: H2 / MySQL dialect differences can hide bugs (e.g., different string comparison semantics)

### Option B: MySQL everywhere (Testcontainers for tests)
- Good: Tests run against the exact production dialect
- Bad: Requires Docker in CI and locally — adds setup friction
- Bad: Testcontainers startup adds 15–25 seconds to each test run

### Option C: H2 file-based everywhere
- Good: Zero setup, data survives restarts
- Bad: Not a production-grade database (no connection pooling, concurrency limits)
- Bad: Still uses H2 dialect — same dialect-difference risk as Option A

## Measurements

Test suite timing (measured 2026-04-20, Java 17.0.16, Maven 3.9.6, Windows 11):

| Setup | Suite Run Time | Notes |
|-------|---------------|-------|
| H2 in-memory (current) | **38.9 seconds** | 13 tests, `@SpringBootTest(RANDOM_PORT)` |
| MySQL via Testcontainers (estimated) | ~55–65 seconds | +15–25s container startup on cold run |

**Conclusion:** H2 saves approximately 35–40% of CI test time vs a containerized MySQL approach. For a 13-test suite this is a 16–26 second saving per run. The trade-off (H2 dialect differences) is acceptable given the query surface is limited to 5 standard JPA methods with no DB-specific SQL.

# ADR-0003: Integration tests only — no unit tests with mocks

**Date:** 2026-04-20  
**Status:** Accepted  
**Deciders:** Jorge Aguilar

---

## Context and Problem Statement

The API has three layers: `TareaController`, `TareaService`, and `TareaRepository`. Each layer can be tested in isolation with mocks, or all layers can be tested together end-to-end via the real HTTP stack. A decision must be made on the testing strategy given the scope and complexity of the current implementation.

## Decision Drivers

- Coverage of real behavior: tests must verify HTTP status codes, response bodies, and validation behavior
- Speed: test suite must run in a reasonable time for CI feedback
- Maintenance cost: test code must not duplicate the production code structure
- Confidence: a green test run must mean the API actually works

## Considered Options

- **Option A:** Integration tests only with `@SpringBootTest(RANDOM_PORT)` (chosen)
- **Option B:** Unit tests per layer (Mockito mocks) + integration tests for critical flows
- **Option C:** Unit tests only (mocked repositories throughout)

## Decision Outcome

Chosen option: **Option A**, because the service and controller layers are thin orchestrators with no independent logic that warrants isolated testing.

`TareaService` has 5 methods, all of which are single-line delegations to `TareaRepository`. `TareaController` maps HTTP verbs to service calls and maps results to `ResponseEntity`. The only non-trivial logic is in the `Tarea` entity (`@PrePersist`, `@PreUpdate`), which is exercised naturally by integration tests.

### Positive Consequences

- 12 integration tests cover all 6 endpoints × success and error cases
- Each test exercises the real HTTP stack: serialization, validation, JPA, response codes
- No mock setup — tests are easy to read and maintain
- Single test class covers the entire API surface

### Negative Consequences

- Integration tests are slower (~3s/test vs ~100ms for unit tests)
- Spring context startup adds ~8s overhead per test run
- When a test fails, all layers are implicated — harder to isolate the root cause
- Low granularity: if `TareaService.actualizar` breaks, the test named `actualizarTarea_existente_debeRetornarTareaActualizada` fails, but you must read the stack trace to find which layer failed

## Pros and Cons of the Options

### Option A: Integration tests only
- Good: Tests verify real end-to-end behavior (HTTP status codes, JSON serialization, JPA lifecycle)
- Good: Simple test code — no mock setup, no argument captors
- Bad: Slower suite (~40s for 13 tests including Spring context startup)
- Bad: Root-cause isolation requires stack trace inspection

### Option B: Unit tests per layer + integration tests for critical flows
- Good: Fast feedback on isolated regressions
- Good: Easier to pinpoint which layer broke
- Bad: Significant mock setup for a thin service layer that just delegates
- Bad: Mocks can diverge from real behavior (mock `TareaRepository.save` may not reflect JPA lifecycle)

### Option C: Unit tests only
- Good: Fastest test execution
- Bad: Does not verify HTTP serialization, validation, or response codes
- Bad: Mockito mocks for `TareaRepository` would give false confidence while real DB interactions are untested

## Measurements

Test suite timing (measured 2026-04-20, Java 17.0.16, Maven 3.9.6, Windows 11):

| Metric | Value |
|--------|-------|
| Total test suite time | **38.9 seconds** |
| Spring context startup | ~8 seconds (shared across all tests via context cache) |
| Average per-test execution | **~2.4 seconds** for integration tests |
| Estimated unit test execution | ~100ms per test (if mocked) |

**Conclusion:** For 13 tests on a 5-endpoint API, the 38.9-second suite is acceptable for CI. If the project grows to 50+ endpoints, the re-evaluation point would be when CI feedback exceeds 3 minutes — at that point, a hybrid unit + integration strategy would be worth the additional complexity.

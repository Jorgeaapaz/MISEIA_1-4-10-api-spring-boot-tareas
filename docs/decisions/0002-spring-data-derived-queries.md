# ADR-0002: Spring Data derived query vs explicit @Query

**Date:** 2026-04-20  
**Status:** Accepted  
**Deciders:** Jorge Aguilar

---

## Context and Problem Statement

The API needs to filter tasks by their `completada` (boolean) field for the `GET /api/tareas/estado/{completada}` endpoint. The `TareaRepository` must expose a method that queries by this field. There are several ways to declare this in Spring Data JPA.

## Decision Drivers

- Minimal boilerplate for simple queries
- Readability: the query intent should be obvious from the code
- No SQL in Java code for trivial equality checks
- Easy to test — the repository method should be covered by existing integration tests

## Considered Options

- **Option A:** Spring Data derived query `findByCompletada(boolean completada)` (chosen)
- **Option B:** Explicit JPQL with `@Query("SELECT t FROM Tarea t WHERE t.completada = :completada")`
- **Option C:** `JpaSpecificationExecutor<Tarea>` for dynamic filtering

## Decision Outcome

Chosen option: **Option A**, because the filter is a single-field equality check — exactly the use case Spring Data derived queries are designed for.

### Positive Consequences

- Zero boilerplate: the method name IS the specification
- Spring Data JPA validates the method name at startup (typos cause a startup failure, not a runtime error)
- Fully covered by integration tests in `TareaIntegrationTest` (tests 10 and 11)

### Negative Consequences

- Derived queries become unreadable for complex predicates (joins, OR clauses, subqueries)
- Method names can become very long for multi-field filters (e.g., `findByCompletadaAndFechaCreacionAfter`)

## Pros and Cons of the Options

### Option A: Derived query `findByCompletada`
- Good: Zero SQL, self-documenting
- Good: Compile-time validation at Spring startup
- Bad: Does not scale to complex queries

### Option B: `@Query("SELECT t FROM Tarea t WHERE t.completada = :completada")`
- Good: Explicit, works for any JPQL complexity
- Bad: Adds noise for a trivial equality check — JPQL string adds no information
- Bad: JPQL syntax is validated at runtime, not compile time

### Option C: `JpaSpecificationExecutor`
- Good: Fully dynamic filters composable at runtime
- Bad: Significant boilerplate (Specification interface, predicate construction)
- Bad: Overkill for a boolean equality filter

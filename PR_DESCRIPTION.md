# PR Description: Add Fluent API for Query Functions in Jakarta NoSQL

## Summary
This Pull Request introduces a structured fluent API for using scalar functions in Jakarta NoSQL queries, aligning with the **Jakarta Query** specification. It allows developers to express richer queries with improved type safety and consistency across NoSQL providers.

Fixes #204

## Motivation
Adding function support at the fluent level:
- Allows users to express richer queries without string-based parsing.
- Improves type safety in query construction.
- Increases developer productivity.
- Ensures deep integration with the Jakarta Query grammar.

## Key Changes

### 1. API Core
- Created `jakarta.nosql.Function`: A new interface providing static factory methods for scalar operations: `UPPER()`, `LOWER()`, `LEFT()`, `RIGHT()`, `LENGTH()`, and `ABS()`.
- Created `jakarta.nosql.UnsupportedFunctionException`: A specialized exception to signal when a specific function is not supported by the underlying database provider.
- Updated `jakarta.nosql.QueryMapper`: Added overloads for `where(Function)`, `and(Function)`, `or(Function)`, and `orderBy(Function)` across `SELECT`, `DELETE`, and `UPDATE` fluent chains.

### 2. TCK (Compatibility Tests)
- Created `Word` entity and corresponding `ArgumentsProvider` suppliers.
- Implemented comprehensive TCK tests in `ee.jakarta.tck.nosql.function` covering all new scalar functions.
- Ensured 100% coverage for string and numeric function operations.

### 3. Documentation
- Updated `spec/src/main/asciidoc/chapters/api/template.adoc` with a new "Query Function Expressions" section.
- Added usage examples and a database support matrix.

## Example Usage

```java
// String function example
List<Word> words = template.select(Word.class)
        .where(Function.upper("meaning"))
        .eq("COFFEE")
        .result();

// Numeric function example
List<Word> highScores = template.select(Word.class)
        .where(Function.abs("score"))
        .gt(50)
        .result();

// Complex query with functions
List<Word> result = template.select(Word.class)
        .where(Function.left("term", 2))
        .eq("Ja")
        .and(Function.length("term"))
        .gt(5)
        .result();
```

## Task Checklist (from Issue #204)
- [x] Design the Function API class (static factory methods)
- [x] Integrate Function into WhereClause expressions
- [x] Update the Template.select() fluent chain to accept function expressions
- [x] Add examples and usage documentation in the specification
- [x] Implement TCK tests for:
    - [x] LEFT()
    - [x] RIGHT()
    - [x] UPPER()
    - [x] LOWER()
    - [x] LENGTH()
    - [x] ABS()

## Quality Assurance
- [x] `mvn clean install` passes successfully.
- [x] No PMD violations found.
- [x] Checkstyle rules followed.
- [x] Javadoc generated without errors.
- [x] All TCK tests pass.

# Spring Boot 4.1 & Java 25 Migration Plan

## Objectives
1. Modernize Spring Boot plugin version to `4.1.0`.
2. Update Java toolchain `languageVersion` to `25`.
3. Update Gradle wrapper to `9.6.1` to support Java 25 bytecode major version 69.
4. Adapt Spring Boot 4.1 test annotations and modular packages:
   - Migrate `@MockBean` to `@MockitoBean` (`org.springframework.test.context.bean.override.mockito.MockitoBean`).
   - Update `AutoConfigureMockMvc` import package (`org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc`).
   - Update `DataJpaTest` import package (`org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`).
   - Update `TestEntityManager` import package (`org.springframework.boot.jpa.test.autoconfigure.TestEntityManager`).
   - Update `TestRestTemplate` import package (`org.springframework.boot.resttestclient.TestRestTemplate`).
   - Add modular Spring Boot 4.1 test starters (`spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`, `spring-boot-resttestclient`, `spring-boot-jpa-test`).
5. Execute `./gradlew check` and verify test suite execution and test coverage >= 90%.

## Execution Status
- Gradle wrapper updated: Completed
- Spring Boot version updated: Completed
- Java 25 toolchain configured: Completed
- Spring Boot 4.1 API deprecations/migrations applied: Completed
- Test suite execution & JaCoCo coverage verified: Completed (Line coverage: 99%, Branch coverage: 92%)

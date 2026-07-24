# Refactor Deprecated Hibernate Properties Plan

## Objective
Update deprecated Hibernate configuration properties to standard Spring Boot JPA properties in `financial-relationship-api`.

## Steps
1. Update `src/main/resources/application.yaml`:
   - Replace `org.hibernate.dialect.MySQL8Dialect` with `org.hibernate.dialect.MySQLDialect`
   - Move `show_sql: true` to `spring.jpa.show-sql: true`
   - Set `spring.jpa.hibernate.ddl-auto: create` (replacing native `hibernate.hbm2ddl.auto`)
2. Update `src/test/resources/application-integration.yaml`:
   - Replace `org.hibernate.dialect.MySQL8Dialect` with `org.hibernate.dialect.MySQLDialect`
   - Move `show_sql: true` to `spring.jpa.show-sql: true`
   - Set `spring.jpa.hibernate.ddl-auto: create` (replacing native `hibernate.hbm2ddl.auto`)
3. Verify `src/test/resources/application.yaml`:
   - Ensure `database-platform` is configured under `spring.jpa.database-platform`
4. Run tests:
   - Execute `./gradlew test` to verify everything passes cleanly.
5. Git commit:
   - Commit changes with message `refactor(config): update deprecated hibernate properties to spring boot standards`.

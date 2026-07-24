# Plan de Corrección de Propiedades YAML Deprecadas - financial-relationship-api

## Objetivos
1. Reemplazar `org.hibernate.dialect.MySQL8Dialect` por `org.hibernate.dialect.MySQLDialect`.
2. Migrar `hibernate.show_sql` a `spring.jpa.show-sql: true`.
3. Consolidar `hbm2ddl.auto` bajo `spring.jpa.hibernate.ddl-auto`.
4. Verificar compilación y tests con `./gradlew test`.

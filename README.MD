# Guía para correr la API con Java, Spring Boot, Docker y MySQL

Aca se detallan las instrucciones detalladas sobre cómo levantar una aplicación Java con Spring Boot utilizando Gradle y Docker, con una base de datos MySQL. También incluye ejemplos de los endpoints para dos controladores en particular.

## Requisitos previos

Antes de comenzar, asegúrese de tener instalados los siguientes componentes en su sistema:

- Java Development Kit (JDK) 17+
- Docker y docker-compose
- Gradle

## Paso 1: Configuración del entorno

1. Clone el repositorio de la aplicación en su máquina local utilizando el siguiente comando:

   ```bash
   git clone https://gitlab.com/cuccoez/rebatest.git
   ```

## Paso 2: Compilación y empaquetado de la aplicación

1. Ejecute el siguiente comando para compilar la aplicación:

   ```bash
   ./gradlew clean build
   ```

2. El archivo JAR resultante se generará en el directorio `build/libs`.

## Paso 3: Construcción de la imagen Docker con docker-compose

1. Ejecute el siguiente comando para construir la imagen Docker de la aplicación y configurar la base de datos de mysql:

   ```bash
   docker-compose up
   ```

2. Espere a que se complete la construcción de la imagen Docker y la ejecución del comando.

## Paso 4: Acceso a Swagger

Abra su navegador web y vaya a la siguiente URL: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

- Algunos Json de ejemplo para consumir las diferentes APIs descritas en la documentación:

### Get a person by their ID
```json
GET /api/people/{id}

Example:
GET /api/people/1
```

### Update an existing person by their ID
```json
PUT /api/people/{id}

Example:
PUT /api/people/1

Request Body:
{
  "name": "John Doe",
  "birthDate": "1990-05-18",
  "nationality": "USA",
  "documentNumber": "123456789",
  "documentType": "PASSPORT",
  "country": {
    "id": 1,
    "name": "United States",
    "code": "US"
  },
  "contactDetails": {
    "address": "123 Main St",
    "phone": "+1 555-1234",
    "email": "john.doe@example.com"
  },
  "pather": null
}
```

### Delete a person by their ID
```json
DELETE /api/people/{id}

Example:
DELETE /api/people/1
```

### Get all people
```json
GET /api/people

Example:
GET /api/people
```

### Create a new person
```json
POST /api/people

Example:
POST /api/people

Request Body:
{
  "name": "Jane Smith",
  "birthDate": "1995-10-20",
  "nationality": "Canada",
  "documentNumber": "987654321",
  "documentType": "PASSPORT",
  "country": {
    "id": 2,
    "name": "Canada",
    "code": "CA"
  },
  "contactDetails": {
    "address": "456 Maple Ave",
    "phone": "+1 555-5678",
    "email": "jane.smith@example.com"
  },
  "pather": null
}
```

### Establish father relationship between two people
```json
POST /api/people/{idFather}/father/{idChild}

Example:
POST /api/people/1/father/2
```

### Get relationship between two people by their IDs
```json
GET /relationships/{id1}/{id2}

Example:
GET /relationships/1/2
```

### Get people statistics by country
```json
GET /api/people/stats

Example:
GET /api/people/stats
```

## Posibles errores

- Si encuentra un error de conexión a la base de datos, verifique que el contenedor de MySQL esté en ejecución y que los detalles de conexión en el archivo `application.yaml` sean correctos.
- Si experimenta errores al compilar o ejecutar la aplicación, asegúrese de tener instalado Java, Docker y Gradle correctamente y de que todas las dependencias del proyecto estén configuradas adecuadamente.
- Si encuentra problemas con los endpoints o el funcionamiento de la aplicación, verifique que los controladores, servicios y modelos estén implementados correctamente y que las rutas y los parámetros de los endpoints coincidan con los proporcionados en este README.

¡Ahora debería poder levantar la API y acceder a Swagger en la siguiente URL: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)!

La app esta publicada aqui: http://reba-cuccoez.sa-east-1.elasticbeanstalk.com/swagger-ui/index.html#/People/getAllPeople

## Notas extras 

Para la prueba de performance use Jmeter, con una prueba sencilla de stast y get person, dejo los archivos para configurarlo más rapido en el raiz del repo.

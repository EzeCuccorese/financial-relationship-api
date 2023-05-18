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
   gradle clean build
   ```

2. El archivo JAR resultante se generará en el directorio `build/libs`.

## Paso 3: Construcción de la imagen Docker con docker-compose

1. Ejecute el siguiente comando para construir la imagen Docker de la aplicación y configurar la base de datos de mysql:

   ```bash
   docker-compose up
   ```

2. Espere a que se complete la construcción de la imagen Docker y la ejecución del comando.

## Paso 4: Acceso a Swagger

1. Abra su navegador web y vaya a la siguiente URL: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

   Asegúrese de que tanto el contenedor de la aplicación como el contenedor de MySQL estén en ejecución.

2. En la pantalla de Swagger, busque y haga clic en el endpoint llamado "createPersona" dentro del controlador "PersonaController".

3. Copie el siguiente JSON y péguelo en el cuadro de entrada del endpoint:

    ```json
       {
           "nombre": "Juan Perez",
           "fechaNacimiento": "2000-01-01",
           "nacionalidad": "Argentino",
           "numeroDocumento": "12345678",
           "tipoDocumento": "DNI",
           "pais": {
               "id": 1,
               "nombre": "Argentina",
               "codigo": "ARG"
           },
          "datosContacto": {
                  "direccion": "Calle Falsa 123",
                  "telefono": "+541112345678",
                  "email": "juan.perez@example.com"
          },
          "relaciones": [], 
          "padre": null
      }
    ```
     
4. Haga clic en el botón "Try it out" para enviar la solicitud y crear una nueva persona.

## Ejemplos de Endpoints

Aquí se presentan ejemplos de los endpoints para dos controladores específicos.

### Controlador: PersonaController

- **Obtener todas las personas**

  - Método: GET
  - URL: `http://localhost:8080/api/personas`

- **Obtener una persona por ID**

  - Método: GET
  - URL: `http://localhost:8080/api/personas/{id}`

  Reemplace `{id}` con el ID de la persona que desea obtener.

- **Crear una nueva persona**

  - Método: POST
  - URL: `http://localhost:8080/api/personas`
  - Cuerpo de la solicitud (ejemplo):

    ```json
    {
        "nombre": "Juan Perez",
        "fechaNacimiento": "2000-01-01",
        "nacionalidad": "Argentino",
        "numeroDocumento": "12345678",
        "tipoDocumento": "DNI",
        "pais": {
            "id": 1,
            "nombre": "Argentina",
            "codigo": "ARG"
        },
        "datosContacto": {
            "direccion": "Calle Falsa 123",
            "telefono": "+541112345678",
            "email": "juan.perez@example.com"
        },
        "relaciones": [],
        "padre": null
    }
    ```

- **Actualizar una persona existente**

  - Método: PUT
  - URL: `http://localhost:8080/api/personas/{id}`
  - Cuerpo de la solicitud (ejemplo):

    ```json
    {
        "nombre": "Nuevo Nombre",
        "fechaNacimiento": "2000-01-01",
        "nacionalidad": "Argentino",
        "numeroDocumento": "12345678",
        "tipoDocumento": "DNI",
        "pais": {
            "id": 1,
            "nombre": "Argentina",
            "codigo": "ARG"
        },
        "datosContacto": {
            "direccion": "Calle Falsa 123",
            "telefono": "+541112345678",
            "email": "juan.perez@example.com"
        },
        "relaciones": [],
        "padre": null
    }
    ```

  Reemplace `{id}` con el ID de la persona que desea actualizar.

- **Eliminar una persona**

  - Método: DELETE
  - URL: `http://localhost:8080/api/personas/{id}`

  Reemplace `{id}` con el ID de la persona que desea eliminar.

- **Establecer padre de una persona**

  - Método: POST
  - URL: `http://localhost:8080/api/personas/{idPadre}/padre/{idHijo}`

  Reemplace `{idPadre}` con el ID del padre y `{idHijo}` con el ID del hijo.

- **Obtener estadísticas de personas**

  - Método: GET
  - URL: `http://localhost:8080/api/personas/stats`

### Controlador: RelacionController

- **Obtener relación entre dos personas**

  - Método: GET
  - URL: `http://localhost:8080/relaciones/{id1}/{id2}`

  Reemplace `{id1}` con el ID de la primera persona y `{id2}` con el ID de la segunda persona.

## Posibles errores

- Si encuentra un error de conexión a la base de datos, verifique que el contenedor de MySQL esté en ejecución y que los detalles de conexión en el archivo `application.yaml` sean correctos.
- Si experimenta errores al compilar o ejecutar la aplicación, asegúrese de tener instalado Java, Docker y Gradle correctamente y de que todas las dependencias del proyecto estén configuradas adecuadamente.
- Si encuentra problemas con los endpoints o el funcionamiento de la aplicación, verifique que los controladores, servicios y modelos estén implementados correctamente y que las rutas y los parámetros de los endpoints coincidan con los proporcionados en este README.

¡Ahora debería poder levantar la API y acceder a Swagger en la siguiente URL: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)!

## Notas extras 

Para la prueba de performance use Jmeter, con una prueba sencilla de stast y get persona, dejo los archivos para configurarlo más rapido en el raiz del repo.
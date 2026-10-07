# Instrucciones de instalación y ejecución

Este proyecto puede ejecutarse de dos formas: **en local** (con los requisitos necesarios instalados en tu máquina) o **con Docker** (sin necesidad de instalar nada más que Docker Desktop).

- Para la **ejecución local**, sigue las secciones [Requisitos local](#requisitos-local), [Instalación para ejecución local](#instalación-para-ejecución-local) y [Ejecución local](#ejecución-local).

- Para la **ejecución con Docker**, sigue las secciones [Requisitos con Docker](#requisitos-con-docker), [Instalación para ejecución con Docker](#instalación-para-ejecución-con-docker) y [Ejecución con Docker](#ejecución-con-docker).

- Para saber **cómo usar la API** una vez arrancada, consulta la sección [Uso de la API](#uso-de-la-api) y los [Casos de uso](#casos-de-uso).

## Requisitos local

- **Java** 21 o superior

- **MySQL Server** 8.0 o superior

- **Maven** 3.9 o superior

- **IDE** recomendado

## Instalación para ejecución local

### 1. Clonar el repositorio

```bash
git clone https://github.com/carlossanchezh/comunidad-vinos-api-rest.git
```

> Si no usas Git, descarga el `.zip` del proyecto y descomprímelo en una carpeta local.

### 2. Crear la base de datos

No hace falta crear la base de datos a mano. El script `database/comunidadvinos.sql` ya incluye las sentencias necesarias:

```sql
DROP DATABASE IF EXISTS `comunidadvinos`;
CREATE DATABASE `comunidadvinos` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `comunidadvinos`;
```

Por tanto, basta con **importar el script** y la base de datos se creará automáticamente con toda su estructura y sus datos de ejemplo. Ese paso se explica en el siguiente apartado.

> Ten en cuenta que el script hace un `DROP DATABASE IF EXISTS` antes de crearla. Si ya tienes una base de datos `comunidadvinos` con datos propios, **se borrará y se reemplazará** por la del script.

### 3. Importar la estructura y los datos

El repositorio incluye `database/comunidadvinos.sql`, un dump con:

- La estructura de las tablas (`usuarios`, `vinos`, `uvas`, `usuario_vino`, `vino_uva`, `seguimientos`).
- Datos de ejemplo (9 usuarios, 21 vinos, 5 uvas, etc.).

Importa el script desde la raíz del proyecto:

```bash
mysql -u root -p < database/comunidadvinos.sql
```

Te pedirá la contraseña de MySQL. Escríbela y pulsa Enter.

### 4. Comprobar que la base de datos está corriendo

Antes de arrancar la aplicación, asegúrate de que **MySQL está en ejecución** y **escuchando en el puerto indicado en `application.properties`** (por defecto, `3306`).

### 5. Configurar las credenciales

Abre `src/main/resources/application.properties` y revisa las credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/comunidadvinos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
```

Por defecto, la app se conecta a `localhost:3306` con usuario `root` y **contraseña vacía**.

#### Opción A: Variables de entorno (recomendado)

Si tu MySQL tiene contraseña, defínela antes de arrancar la aplicación.

**PowerShell (Windows)**:
```powershell
$env:DB_PASSWORD="tu_contraseña"
```

**CMD (Windows)**:
```cmd
set DB_PASSWORD=tu_contraseña
```

**Linux / macOS**:
```bash
export DB_PASSWORD=tu_contraseña
```

Si además tu usuario de MySQL no es `root`:
```bash
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_contraseña
```

#### Opción B: Editar `application.properties`

Sustituye los valores `${DB_...}` por valores fijos:

```properties
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

> **No compartas esta versión** si contiene credenciales reales. Es solo para uso local.

## Ejecución local

### Con Maven (desde la línea de comandos)

#### Compilar el proyecto

Compila el código fuente y deja los `.class` en `target/classes/`:

```bash
mvn compile
```

#### Ejecutar la aplicación

Compila el proyecto (si hace falta) y arranca la aplicación.

```bash
mvn spring-boot:run
```

Cuando veas `Started ComunidadvinosApplication`, ya está lista.

#### Limpiar lo compilado

Borra la carpeta `target/` completa (clases compiladas, `.jar`, recursos copiados, etc.):

```bash
mvn clean
```

Es útil cuando algo se ha quedado corrupto, cuando cambias de rama o cuando quieres forzar una recompilación desde cero.

#### Limpiar y recompilar en un solo comando

Muy habitual cuando quieres asegurarte de que todo está fresco:

```bash
mvn clean compile
```

### Desde un IDE

1. Abre el proyecto en tu IDE.

2. El IDE detectará que es un proyecto Maven e importará las dependencias automáticamente. Espera a que termine; la primera vez puede tardar unos minutos.

3. Ejecuta la clase principal `ComunidadvinosApplication.java` (botón Run).

## Requisitos con Docker

- **Docker Desktop**

## Instalación para ejecución con Docker

### 1. Clonar el repositorio

```bash
git clone https://github.com/carlossanchezh/comunidad-vinos-api-rest.git
```

> Si no usas Git, descarga el `.zip` del proyecto y descomprímelo en una carpeta local.

## Ejecución con Docker

### Levantar los contenedores

Descarga la imagen de la aplicación ([carlossanchezh/comunidadvinos-app](https://hub.docker.com/r/carlossanchezh/comunidadvinos-app)) desde Docker Hub:

```bash
docker pull carlossanchezh/comunidadvinos-app:latest
```

Una vez descargada, levanta los contenedores de la aplicación y de MySQL:

```bash
docker compose up
```

Para ejecutarlo en segundo plano:

```bash
docker compose up -d
```

El proceso que seguirá Docker será el siguiente:

1. Descarga la imagen de la aplicación desde Docker Hub.
2. Descarga la imagen oficial de MySQL 8.
3. Crea la red interna `comunidad-net` y el volumen `db_data`.
4. Arranca el contenedor MySQL e importa automáticamente el dump `database/comunidadvinos.sql`.
5. Espera a que MySQL esté listo y arranca el contenedor de la aplicación.

> Si no tienes conexión a Docker Hub o la imagen no está disponible, `docker compose up` construirá la imagen de la aplicación a partir del `Dockerfile` como alternativa.

Si prefieres **forzar la construcción** de la imagen en lugar de descargarla de Docker Hub:

```bash
docker compose up --build
```

Para ejecutarlo en segundo plano:

```bash
docker compose up --build -d
```

### Ver los logs de los contenedores

Muestra los logs del contenedor de la app en tiempo real:

```bash
docker compose logs -f app
```

Muestra los logs del contenedor de MySQL en tiempo real:

```bash
docker compose logs -f db
```

> Solo es necesario si has levantado los contenedores en segundo plano (`docker compose up --build -d`).

### Ver el estado de los contenedores

Muestra los contenedores del proyecto, su estado y los puertos expuestos:

```bash
docker compose ps
```

### Detener los contenedores

Para los contenedores sin borrar los datos:

```bash
docker compose down
```

Los datos se conservan en el volumen `db_data`. La próxima vez que levantes, seguirán ahí.

Para parar los contenedores y **borrar también el volumen**:

```bash
docker compose down -v
```

> Es lo que usarías si quieres volver a cargar la base de datos a partir de `database/comunidadvinos.sql`.

## Uso de la API

Una vez arrancada la aplicación, la API queda escuchando en el puerto **9000** con el contexto **`/api/v1`**. Todas las peticiones deben ir precedidas por esa base.

El puerto 9000 está definido por defecto en `application.properties` así como el contexto:

```properties
server.port=9000
server.servlet.context-path=/api/v1
```

### Composición de una URL

```text
http://localhost:9000 /api/v1 /usuarios /1
└──────┬─────────────┘└──┬───┘ └──┬───┘ └┬┘
       │                 │        │      │
  Host y puerto       Contexto  Recurso  ID
```

- **Host y puerto**: `localhost:9000`
- **Context path**: `/api/v1`
- **Recurso**: `/usuarios`, `/vinos`, `/uvas`, `/tareas`…
- **ID** (opcional): identificador del recurso concreto

### Tipos de contenido

La API admite **JSON** y **XML** tanto en las peticiones como en las respuestas.

| Cabecera | Valor |
|---|---|
| `Content-Type` | `application/json` o `application/xml` |
| `Accept` | `application/json` o `application/xml` |

### Cómo probar la API

Puedes usar cualquiera de estas herramientas:

- **`curl`** desde la terminal.

- **Postman** o **Insomnia** (clientes REST gráficos).

- **Navegador web**: válido solo para peticiones `GET` (por ejemplo, abrir `http://localhost:9000/api/v1/usuarios` en el navegador).

## Casos de uso

A continuación se muestran ejemplos de las operaciones con JSON.

> Los ejemplos asumen que la base de datos contiene los datos de ejemplo del dump `comunidadvinos.sql`. Si ejecutas operaciones que modifican o eliminan recursos (PUT, DELETE), los ejemplos posteriores pueden fallar. Para volver al estado inicial, reimporta el `.sql`

### Usuarios

#### 1. Registrar un usuario

**Caso exitoso**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Lucía Fernández",
    "correo": "lucia.fernandez@example.com",
    "fechaNacimiento": "1995-03-22"
  }'
```

**Respuesta esperada**: `201 Created` con cabecera `Location` apuntando al recurso creado.

**Error: usuario menor de edad**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Niño Test",
    "correo": "nino@example.com",
    "fechaNacimiento": "2015-01-01"
  }'
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

**Error: correo duplicado**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Otro Juan",
    "correo": "juan.perez@gmail.com",
    "fechaNacimiento": "1990-05-15"
  }'
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

#### 2. Listar todos los usuarios

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/usuarios
```

**Respuesta esperada**: `200 OK` con la representación paginada de los usuarios (5 usuarios por página por defecto).

**Con filtro por nombre**:

```bash
curl "http://localhost:9000/api/v1/usuarios?filtro=Mar"
```

**Respuesta esperada**: `200 OK` con la representación paginada de los usuarios con el filtro aplicado a su nombre (5 usuarios por página por defecto).

**Con paginación personalizada**:

```bash
curl "http://localhost:9000/api/v1/usuarios?page=0&size=3"
```
**Respuesta esperada**: `200 OK` con la representación paginada de los usuarios ajustada a 3 usuarios por página.

#### 3. Obtener un usuario concreto

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/usuarios/1
```

**Respuesta esperada**: `200 OK` con la representación del recurso usuario (datos del usuario + enlaces HATEOAS).

**Error: usuario no encontrado**:
```bash
curl http://localhost:9000/api/v1/usuarios/9999
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

#### 4. Actualizar un usuario

**Caso exitoso**:

```bash
curl -X PUT http://localhost:9000/api/v1/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez Actualizado",
    "correo": "juan.perez@example.com",
    "fechaNacimiento": "1990-05-15"
  }'
```

**Respuesta esperada**: `204 No Content`.

**Error: usuario no encontrado**:

```bash
curl -X PUT http://localhost:9000/api/v1/usuarios/9999 \
  -H "Content-Type: application/json" \
  -d '{ "nombre": "X", "correo": "x@x.com", "fechaNacimiento": "1990-01-01" }'
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

#### 5. Eliminar un usuario

**Caso exitoso**:

```bash
curl -X DELETE http://localhost:9000/api/v1/usuarios/1
```

**Respuesta esperada**: `204 No Content`.

**Error: usuario no encontrado**:
```bash
curl -X DELETE http://localhost:9000/api/v1/usuarios/9999
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

### Vinos de un usuario

#### 6. Añadir un vino a la lista de un usuario

**Caso exitoso**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios/1/vinos \
  -H "Content-Type: application/json" \
  -d '{
    "vinoId": 3,
    "puntuacion": 9
  }'
```

**Respuesta esperada**: `201 Created` con cabecera `Location` apuntando al recurso creado.

**Error: vino ya en la lista**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios/1/vinos \
  -H "Content-Type: application/json" \
  -d '{ "vinoId": 3, "puntuacion": 8 }'
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

**Error: vino no existe**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios/1/vinos \
  -H "Content-Type: application/json" \
  -d '{ "vinoId": 9999, "puntuacion": 8 }'
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

**Error: usuario no existe**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios/9999/vinos \
  -H "Content-Type: application/json" \
  -d '{ "vinoId": 1, "puntuacion": 8 }'
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

#### 7. Listar los vinos de un usuario

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/usuarios/1/vinos
```

**Respuesta esperada**: `200 OK` con la representación paginada de los vinos del usuario.

**Con filtros**:
```bash
curl "http://localhost:9000/api/v1/usuarios/1/vinos?tipo=Tinto&anada=2018&page=0&size=5"
```

**Respuesta esperada**: `200 OK` con la representación paginada de los vinos del usuario aplicando los filtros.

Parámetros disponibles: `fechaDesde`, `fechaHasta`, `tipo`, `origen`, `anada`, `bodega`, `uva`, `page`, `size`.

**Error: usuario no encontrado**:

```bash
curl http://localhost:9000/api/v1/usuarios/9999/vinos
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

#### 8. Modificar la puntuación de un vino

**Caso exitoso**:

```bash
curl -X PUT http://localhost:9000/api/v1/usuarios/1/vinos/3 \
  -H "Content-Type: application/json" \
  -d '{ "puntuacion": 7 }'
```

**Respuesta esperada**: `204 No Content`.

**Error: vino no pertenece a la lista**:

```bash
curl -X PUT http://localhost:9000/api/v1/usuarios/1/vinos/9999 \
  -H "Content-Type: application/json" \
  -d '{ "puntuacion": 7 }'
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

#### 9. Eliminar un vino de la lista de un usuario

**Caso exitoso**:

```bash
curl -X DELETE http://localhost:9000/api/v1/usuarios/1/vinos/3
```

**Respuesta esperada**: `204 No Content`.

**Error: vino no pertenece a la lista**:

```bash
curl -X DELETE http://localhost:9000/api/v1/usuarios/1/vinos/9999
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

### Seguimientos

#### 10. Solicitar seguir a otro usuario

**Caso exitoso**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios/1/seguidos \
  -H "Content-Type: application/json" \
  -d '{ "seguidoId": 4 }'
```

**Respuesta esperada**: `202 Accepted` con `Location: /tareas/{taskId}` y el cuerpo con la tarea. **Guarda el `taskId`** para los siguientes pasos.

**Error: seguirse a sí mismo**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios/1/seguidos \
  -H "Content-Type: application/json" \
  -d '{ "seguidoId": 1 }'
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

**Error: seguimiento ya existe**:

```bash
curl -X POST http://localhost:9000/api/v1/usuarios/1/seguidos \
  -H "Content-Type: application/json" \
  -d '{ "seguidoId": 2 }'
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

**Error: usuario no existe**:
```bash
curl -X POST http://localhost:9000/api/v1/usuarios/9999/seguidos \
  -H "Content-Type: application/json" \
  -d '{ "seguidoId": 1 }'
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

#### 11. Listar los usuarios que sigue un usuario

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/usuarios/1/seguidos
```

**Respuesta esperada**: `200 OK` con la representación paginada de los usuarios seguidos.

**Con filtro por nombre**:

```bash
curl "http://localhost:9000/api/v1/usuarios/1/seguidos?filtro=Ana"
```

**Respuesta esperada**: `200 OK` con la representación paginada de los usuarios seguidos aplicando el filtro.

**Error: usuario no encontrado**:

```bash
curl http://localhost:9000/api/v1/usuarios/9999/seguidos
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

#### 12. Consultar las solicitudes pendientes

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/usuarios/4/solicitudes
```

**Respuesta esperada**: `200 OK` con la representación paginada de las solicitudes pendientes del usuario.

**Error: usuario no encontrado**:
```bash
curl http://localhost:9000/api/v1/usuarios/9999/solicitudes
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

#### 13. Aceptar una solicitud de seguimiento

**Caso exitoso**:

```bash
curl -X PUT http://localhost:9000/api/v1/usuarios/4/solicitudes/1 \
  -H "Content-Type: application/json" \
  -d '{ "estado": "ACEPTADA" }'
```

**Respuesta esperada**: `204 No Content`.

**Alternativa con rechazo**:

```bash
curl -X PUT http://localhost:9000/api/v1/usuarios/4/solicitudes/1 \
  -H "Content-Type: application/json" \
  -d '{ "estado": "RECHAZADA" }'
```
**Respuesta esperada**: `204 No Content`.

**Error: solicitud no pendiente**:

```bash
curl -X PUT http://localhost:9000/api/v1/usuarios/4/solicitudes/1 \
  -H "Content-Type: application/json" \
  -d '{ "estado": "ACEPTADA" }'
```

**Respuesta esperada**: Si ya se había aceptado o rechazado antes, devuelve `400 Bad Request` con mensaje de error.

**Error: estado no válido**:
```bash
curl -X PUT http://localhost:9000/api/v1/usuarios/4/solicitudes/1 \
  -H "Content-Type: application/json" \
  -d '{ "estado": "PENDIENTE" }'
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

**Error: solicitud no existe**:

```bash
curl -X PUT http://localhost:9000/api/v1/usuarios/4/solicitudes/9999 \
  -H "Content-Type: application/json" \
  -d '{ "estado": "ACEPTADA" }'
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

#### 14. Dejar de seguir a un usuario

**Caso exitoso**:

```bash
curl -X DELETE http://localhost:9000/api/v1/usuarios/1/seguidos/4
```

**Respuesta esperada**: `204 No Content`.

**Error: no sigues a ese usuario**:

```bash
curl -X DELETE http://localhost:9000/api/v1/usuarios/1/seguidos/9999
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

**Error: seguirse a sí mismo**:

```bash
curl -X DELETE http://localhost:9000/api/v1/usuarios/1/seguidos/1
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

#### 15. Consultar los vinos de un usuario seguido

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/usuarios/1/seguidos/4/vinos
```

**Respuesta esperada**: `200 OK` con la representación paginada de los vinos del usuario seguido.

**Con filtros**:
```bash
curl "http://localhost:9000/api/v1/usuarios/1/seguidos/4/vinos?tipo=Tinto&page=0&size=5"
```

**Respuesta esperada**: `200 OK` con la representación paginada de los vinos del usuario seguido con los filtros aplicados.

**Error: no sigues a ese usuario**:
```bash
curl http://localhost:9000/api/v1/usuarios/1/seguidos/9999/vinos
```

**Respuesta esperada**: `400 Bad Request` con mensaje de error.

### Tareas de seguimiento

#### 16. Consultar el estado de una tarea

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/tareas/1
```

**Respuesta esperada**: `200 OK` con la representación del recurso tarea si está `PENDIENTE` o `RECHAZADA`. Si la tarea está `ACEPTADA`, devuelve `303 See Other` con `Location` apuntando al recurso resultante.

**Error: tarea no encontrada**:

```bash
curl http://localhost:9000/api/v1/tareas/9999
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

### Vinos

#### 17. Obtener un vino concreto

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/vinos/1
```

**Respuesta esperada**: `200 OK` con la representación del recurso vino, incluyendo su composición de uvas con porcentajes.

**Error: vino no encontrado**:
```bash
curl http://localhost:9000/api/v1/vinos/9999
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

### Uvas

#### 18. Obtener una uva concreta

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/uvas/1
```

**Respuesta esperada**: `200 OK` con la representación del recurso uva (nombre y descripción).

**Error: uva no encontrada**:
```bash
curl http://localhost:9000/api/v1/uvas/9999
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

### Recomendaciones y estadísticas

#### 19. Obtener recomendaciones de un usuario

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/usuarios/1/recomendaciones
```

**Respuesta esperada**: `200 OK` con:
- Los datos del usuario.
- Sus 5 últimos vinos añadidos.
- Sus 5 vinos mejor puntuados.
- Los 5 mejores vinos de sus amigos.

**Error: usuario no encontrado**:

```bash
curl http://localhost:9000/api/v1/usuarios/9999/recomendaciones
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

#### 20. Consultar estadísticas de un usuario

**Caso exitoso**:

```bash
curl http://localhost:9000/api/v1/usuarios/1/estadisticas
```

**Respuesta esperada**: `200 OK` con la puntuación media del usuario.

**Con filtros**:
```bash
curl "http://localhost:9000/api/v1/usuarios/1/estadisticas?tipo=Tinto&anada=2018"
```

**Respuesta esperada**: `200 OK` con la puntuación media del usuario aplicando los filtros a los vinos del usuario.

Parámetros disponibles: `fechaDesde`, `fechaHasta`, `tipo`, `origen`, `anada`, `bodega`, `uva`.

**Error: usuario no encontrado**:

```bash
curl http://localhost:9000/api/v1/usuarios/9999/estadisticas
```

**Respuesta esperada**: `404 Not Found` con mensaje de error.

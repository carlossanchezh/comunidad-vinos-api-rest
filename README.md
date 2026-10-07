# Comunidad Vinos API REST

## Descripción

API REST para una comunidad online de gestión y recomendación de vinos. Los usuarios pueden registrarse, puntuar vinos, seguir a otros usuarios, consultar recomendaciones personalizadas y obtener estadísticas de sus puntuaciones.

El servicio simula una comunidad online donde los usuarios interactúan alrededor del mundo del vino. Cada usuario puede:

- Registrarse en la comunidad.
- Gestionar su perfil: consultar, modificar y eliminar sus datos.
- Añadir vinos a su lista personal, puntuarlos, modificarlos y eliminarlos.
- Consultar los vinos de otros usuarios a los que sigue, con filtros por características del vino.
- Solicitar seguir a otros usuarios.
- Dejar de seguir a usuarios que ya sigue.
- Gestionar sus solicitudes de seguimiento pendientes.
- Obtener recomendaciones personalizadas basadas en sus gustos y en los de sus amigos.
- Consultar estadísticas de sus puntuaciones con filtros por fecha, tipo, origen, añada, bodega o uva.

## Arquitectura

### Diseño

La API se ha diseñado siguiendo los principios **RESTful**:

- Los **recursos** se identifican mediante **URIs** claras y jerárquicas.

- Se utiliza el **interfaz uniforme HTTP** con los verbos `GET`, `POST`, `PUT` y `DELETE` según la semántica correspondiente.

- La comunicación se realiza principalmente en **JSON** (y opcionalmente XML), mediante los tipos MIME `application/json` y `application/xml`.

- Se aplica **HATEOAS**: cada representación incluye enlaces (`_links`) que permiten navegar entre recursos relacionados, de modo que el cliente descubre dinámicamente las operaciones disponibles.

- Los errores se devuelven con **códigos HTTP apropiados** acompañados de un cuerpo con un mensaje descriptivo.

- Las **operaciones asíncronas** (solicitud de seguimiento) se modelan como recursos de tipo **tarea**, devolviendo `202 Accepted` con la `Location` de la tarea creada.

### Decisiones de diseño

- **Arquitectura en capas** (Controller → Service → Repository → Model) para separar responsabilidades y facilitar el mantenimiento y las pruebas.

- **HATEOAS con `RepresentationModelAssemblerSupport`** para cada recurso, de modo que la lógica de generación de enlaces queda aislada de los controladores.

- **DTOs específicos** (`SeguidoData`, `SolicitudPendienteData`, `VinoPuntuacionData`, `UvaPorcentajeData`, `EstadisticasData`, `RecomendacionesData`) para no exponer directamente las entidades JPA y controlar exactamente qué información se devuelve en cada operación.

- **Manejo centralizado de excepciones** con `@RestControllerAdvice`. Cada tipo de error tiene su propia excepción y su propia clase `*ExceptionAdvice` que la traduce a un código HTTP concreto y a un cuerpo `ErrorMessage`.

- **Operación de seguimiento asíncrona**: seguir a otro usuario **no es inmediato**, se implementa mediante un recurso `TareaSeguimiento` gestionado en memoria (`ConcurrentHashMap`). El usuario que quiere seguir recibe un `202 Accepted` con la URI de la tarea, y el seguido acepta o rechaza la solicitud actualizándola.

- **Claves primarias compuestas** con `@EmbeddedId` para las relaciones muchos-a-muchos con atributos (`SeguimientoId`, `UsuarioVinoId`, `VinoUvaId`).

- **Paginación y filtrado** en todos los listados mediante `Pageable` y `PagedResourcesAssembler`, exponiendo los enlaces `first`, `prev`, `next`, `last` de forma automática.

### Reglas de negocio

La API aplica las siguientes reglas de negocio:

#### Usuarios

| Regla | Descripción |
|---|---|
| **Mayoría de edad** | Un usuario debe tener **18 años o más** para poder registrarse. Si no, se rechaza la creación. |
| **Correo único** | El correo electrónico debe ser único en el sistema. No puede haber dos usuarios con el mismo correo. |

#### Vino–uva

| Regla | Descripción |
|---|---|
| **Porcentaje válido** | El porcentaje de una uva en un vino debe estar entre `1` y `100`. |

#### Usuario-Vino

| Regla | Descripción |
|---|---|
| **Puntuación válida** | La puntuación de un vino debe estar entre `0` y `10`. |
| **Vino no duplicado** | Un usuario no puede añadir dos veces el mismo vino a su lista. |
| **Vino pertenece a la lista** | Solo se puede modificar o eliminar un vino que ya esté en la lista del usuario. |

#### Seguimientos

| Regla | Descripción |
|---|---|
| **No seguirse a uno mismo** | Un usuario no puede solicitar seguirse a sí mismo. |
| **Seguimiento único** | No puede existir más de una relación de seguimiento entre los mismos dos usuarios. |
| **Solicitud pendiente** | Solo se puede aceptar o rechazar una solicitud que esté en estado `PENDIENTE`. |
| **Estado válido** | Al actualizar una solicitud, el nuevo estado debe ser `ACEPTADA` o `RECHAZADA`. |
| **Seguimiento existente** | Para dejar de seguir a alguien, debe existir antes la relación de seguimiento. |

### Capas

El proyecto sigue una arquitectura en capas clásica de Spring Boot:

| Capa | Paquete | Responsabilidad |
|---|---|---|
| **Controladores** | `controller` | Exponen los endpoints REST, validan la entrada a alto nivel, delegan en los servicios y construyen las respuestas HTTP. |
| **Assemblers** | `assembler` | Convierten entidades y DTOs en representaciones con enlaces hipermedia. |
| **Servicios** | `service` | Contienen la lógica de negocio, aplican reglas, coordinan repositorios y ensamblan los resultados. |
| **Repositorios** | `repository` | Interfaces Spring Data JPA que abstraen el acceso a la base de datos. Incluyen consultas JPQL personalizadas con `@Query`. |
| **Modelo** | `model` | Entidades JPA, DTOs de representación, claves compuestas y enumeraciones. |
| **Excepciones** | `exception` | Excepciones personalizadas y `@RestControllerAdvice` que las traducen a respuestas HTTP. |

### Componentes del sistema

El sistema está formado por **dos componentes principales** que se ejecutan de forma independiente y se comunican a través de la red interna de la máquina: la **API REST** y la **base de datos MySQL**.

#### 1. API REST (aplicación Spring Boot)

Es el componente central del sistema. Se encarga de exponer toda la lógica de negocio a través de endpoints HTTP, validar las peticiones, aplicar las reglas del dominio y devolver respuestas hipermedia.

| Característica | Valor |
|---|---|
| **Framework** | Spring Boot 4.0.3 |
| **Lenguaje** | Java 21 |
| **Puerto de escucha** | `9000` |
| **Context path** | `/api/v1` |
| **URL base** | `http://localhost:9000/api/v1` |
| **Tipos MIME soportados** | `application/json`, `application/xml` |
| **Punto de entrada** | `ComunidadvinosApplication.java` (clase anotada con `@SpringBootApplication`) |

La API recibe las peticiones HTTP, las enruta a través de los controladores (`@RestController`), aplica la lógica de negocio en los servicios (`@Service`), accede a la base de datos mediante los repositorios (`@Repository`) y devuelve las respuestas con enlaces HATEOAS.

#### 2. Base de datos MySQL

Almacena de forma persistente toda la información del dominio: usuarios, vinos, uvas y las relaciones entre ellos.

| Característica | Valor |
|---|---|
| **Motor** | MySQL 8+ |
| **Puerto** | `3306` |
| **Nombre de la base de datos** | `comunidadvinos` |
| **Codificación** | `utf8mb4` / `utf8mb4_0900_ai_ci` |
| **Tablas** | `usuarios`, `vinos`, `uvas`, `usuario_vino`, `vino_uva`, `seguimientos` |

#### 3. Comunicación entre componentes

La API se conecta a MySQL mediante **JDBC** usando el driver `com.mysql.cj.jdbc.Driver`. Spring Data JPA gestiona la capa de persistencia por encima de JDBC, traduciendo entidades y repositorios a SQL.

### Modelo de datos

El sistema se apoya en las siguientes entidades principales:

| Entidad | Tabla | Descripción |
|---|---|---|
| `Usuario` | `usuarios` | Usuario de la comunidad con nombre, fecha de nacimiento y correo. |
| `Vino` | `vinos` | Vino con nombre, bodega, añada, origen y tipo. |
| `Uva` | `uvas` | Tipo de uva con nombre y descripción. |
| `UsuarioVino` | `usuario_vino` | Relación usuario–vino con puntuación y fecha de adición. |
| `VinoUva` | `vino_uva` | Relación vino–uva con porcentaje de composición. |
| `Seguimiento` | `seguimientos` | Relación de seguimiento entre dos usuarios con fecha de solicitud. |

#### Relaciones principales

| Relación | Cardinalidad | Tabla intermedia | 
|---|---|---|
| Vino ↔ Uva | N:M | `vino_uva` |
| Usuario ↔ Vino | N:M | `usuario_vino` | 
| Usuario ↔ Usuario (reflexiva) | N:M | `seguimientos` | 

#### Diagrama entidad-relación

```text
┌────────────────────┐          ┌────────────────────┐          ┌────────────────────┐
│     usuarios       │          │   usuario_vino     │          │       vinos        │
├────────────────────┤          ├────────────────────┤          ├────────────────────┤
│ id (PK)            │ 1      N │ id (PK)            │ N      1 │ id (PK)            │
│ nombre             │──────────│ usuario_id (FK)    │──────────│ nombre             │
│ fecha_nacimiento   │          │ vino_id (FK)       │          │ bodega             │
│ correo (UNIQUE)    │          │ puntuacion (0-10)  │          │ anada              │
└────────────────────┘          │ fecha_anadido      │          │ origen             │
        │                       └────────────────────┘          │ tipo               │
        │ 1                                                     └────────────────────┘
        │ N                                                              │ 1
┌────────────────────┐                                          ┌────────────────────┐
│   seguimientos     │                                          │     vino_uva       │
├────────────────────┤                                          ├────────────────────┤
│ id (PK)            │                                          │ id (PK)            │
│ seguidor_id (FK)   │                                          │ vino_id (FK)       │
│ seguido_id (FK)    │                                          │ uva_id (FK)        │
│ fecha_solicitud    │                                          │ porcentaje (1-100) │
└────────────────────┘                                          └────────────────────┘
                                                                          │ N
                                                                          │
                                                                ┌────────────────────┐
                                                                │       uvas         │
                                                                ├────────────────────┤
                                                                │ id (PK)            │
                                                                │ nombre (UNIQUE)    │
                                                                │ descripcion        │
                                                                └────────────────────┘
```

### Endpoints

Todas las URIs están precedidas por el contexto base `/api/v1`. Los recursos se organizan de forma jerárquica siguiendo los principios REST.

#### Usuarios

| Método | URI | Descripción |
|---|---|---|
| `POST` | `/usuarios` | Crear un nuevo usuario en la comunidad. |
| `GET` | `/usuarios` | Listar todos los usuarios, con filtro opcional por patrón de nombre y paginación. |
| `GET` | `/usuarios/{id}` | Obtener los datos básicos de un usuario concreto. |
| `PUT` | `/usuarios/{id}` | Actualizar los datos básicos de un usuario. |
| `DELETE` | `/usuarios/{id}` | Eliminar el perfil de un usuario. |

#### Vinos de un usuario

| Método | URI | Descripción |
|---|---|---|
| `POST` | `/usuarios/{usuarioId}/vinos` | Añadir un vino a la lista personal del usuario con una puntuación. |
| `GET` | `/usuarios/{usuarioId}/vinos` | Listar los vinos del usuario con filtros opcionales y paginación. |
| `PUT` | `/usuarios/{usuarioId}/vinos/{vinoId}` | Modificar la puntuación de un vino de la lista del usuario. |
| `DELETE` | `/usuarios/{usuarioId}/vinos/{vinoId}` | Eliminar un vino de la lista del usuario. |

#### Seguimientos

| Método | URI | Descripción |
|---|---|---|
| `POST` | `/usuarios/{seguidorId}/seguidos` | Solicitar seguir a otro usuario (operación asíncrona). |
| `GET` | `/usuarios/{usuarioId}/seguidos` | Listar los usuarios que sigue un usuario, con filtro por nombre y paginación. |
| `GET` | `/usuarios/{seguidoId}/solicitudes` | Listar las solicitudes de seguimiento pendientes recibidas. |
| `PUT` | `/usuarios/{seguidoId}/solicitudes/{seguidorId}` | Aceptar o rechazar una solicitud de seguimiento. |
| `DELETE` | `/usuarios/{seguidorId}/seguidos/{seguidoId}` | Dejar de seguir a un usuario. |
| `GET` | `/usuarios/{seguidorId}/seguidos/{seguidoId}/vinos` | Listar los vinos de un usuario al que se sigue, con filtros y paginación. |

#### Recomendaciones y estadísticas

| Método | URI | Descripción |
|---|---|---|
| `GET` | `/usuarios/{id}/recomendaciones` | Obtener recomendaciones personalizadas: datos del usuario, 5 últimos vinos, 5 mejores vinos y 5 mejores vinos de sus amigos. |
| `GET` | `/usuarios/{id}/estadisticas` | Obtener la puntuación media del usuario con filtros opcionales. |

#### Vinos

| Método | URI | Descripción |
|---|---|---|
| `GET` | `/vinos/{id}` | Obtener un vino con su composición de uvas. |

#### Uvas

| Método | URI | Descripción |
|---|---|---|
| `GET` | `/uvas/{id}` | Obtener una uva. |

#### Tareas de seguimiento

| Método | URI | Descripción |
|---|---|---|
| `GET` | `/tareas/{taskId}` | Consultar el estado de una tarea de seguimiento. Si está aceptada, redirige al recurso resultante (`303 See Other`). |

#### Parámetros de consulta (solo `GET`)

| Parámetro | Aplica a | Descripción |
|---|---|---|
| `filtro` | `/usuarios`, `/usuarios/{id}/seguidos` | Patrón de nombre a buscar. |
| `page` | Todos los listados | Número de página (por defecto `0`). |
| `size` | Todos los listados | Tamaño de página (por defecto `5`). |
| `fechaDesde` | `/usuarios/{id}/vinos`, `/usuarios/{id}/estadisticas`, `/usuarios/{id}/seguidos/{sid}/vinos` | Fecha mínima de adición del vino. |
| `fechaHasta` | `/usuarios/{id}/vinos`, `/usuarios/{id}/estadisticas`, `/usuarios/{id}/seguidos/{sid}/vinos`  | Fecha máxima de adición del vino. |
| `tipo` | `/usuarios/{id}/vinos`, `/usuarios/{id}/estadisticas`, `/usuarios/{id}/seguidos/{sid}/vinos`  | Tipo de vino (tinto, blanco, rosado, espumoso, generoso, dulce…). |
| `origen` | `/usuarios/{id}/vinos`, `/usuarios/{id}/estadisticas`, `/usuarios/{id}/seguidos/{sid}/vinos`  | Origen o denominación del vino. |
| `anada` | `/usuarios/{id}/vinos`, `/usuarios/{id}/estadisticas`, `/usuarios/{id}/seguidos/{sid}/vinos`  | Año de la cosecha. |
| `bodega` | `/usuarios/{id}/vinos`, `/usuarios/{id}/estadisticas`, `/usuarios/{id}/seguidos/{sid}/vinos`  | Nombre de la bodega (búsqueda parcial). |
| `uva` | `/usuarios/{id}/vinos`, `/usuarios/{id}/estadisticas`, `/usuarios/{id}/seguidos/{sid}/vinos`  | Nombre del tipo de uva (búsqueda parcial). |

### Códigos HTTP utilizados

| Código | Nombre | Método | Dónde aparece |
|---|---|---|---|
| `200` | OK | `GET` | Al obtener correctamente cualquier recurso. |
| `201` | Created | `POST` | Al crear un recurso nuevo. Devuelve `Location`. |
| `202` | Accepted | `POST` | Al solicitar un seguimiento (operación asíncrona). |
| `204` | No Content | `PUT` / `DELETE` | Al actualizar o eliminar sin devolver cuerpo. |
| `303` | See Other | `GET` | Al consultar una tarea ya aceptada (redirige al resultado). |
| `400` | Bad Request | Todos | Cuando se incumple una regla de negocio. |
| `404` | Not Found | Todos | Cuando el recurso no existe. |

### Funcionamiento

A continuación se describe el flujo completo de una petición típica, desde que el cliente la envía hasta que recibe la respuesta, con referencias a los archivos y métodos concretos del código.

#### Flujo general de una petición

1. **Recepción de la petición HTTP**  
   El cliente envía una petición a `http://localhost:9000/api/v1/...`. Spring Boot la enruta al método correspondiente del controlador anotado con `@RestController` y `@RequestMapping`.

2. **Controlador**  
   El controlador valida los parámetros de entrada (`@PathVariable`, `@RequestParam`, `@RequestBody`) y delega la lógica en el servicio correspondiente.

3. **Servicio**  
   El servicio (`UsuarioService`, `VinoService`, `UsuarioVinoService`, `SeguimientoService`, `TareaSeguimientoService`) aplica las reglas de negocio, comprueba precondiciones y llama a los repositorios.

4. **Repositorio**  
   Los repositorios (`UsuarioRepository`, `VinoRepository`, `UsuarioVinoRepository`, `SeguimientoRepository`, `VinoUvaRepository`, `UvaRepository`) extienden `JpaRepository` y ejecutan las consultas. Algunas son consultas JPQL personalizadas con `@Query`.

5. **Base de datos MySQL**  
   Hibernate traduce las operaciones JPA a SQL y las ejecuta contra MySQL. Los resultados vuelven al repositorio como entidades Java.

6. **Ensamblado de la respuesta (HATEOAS)**  
   Los assemblers (`UsuarioModelAssembler`, `VinoPuntuacionDataModelAssembler`, `SeguidoDataModelAssembler`, `SolicitudPendienteDataModelAssembler`, `UvaPorcentajeDataModelAssembler`) convierten las entidades en DTOs y añaden los enlaces `_links` que permiten navegar por el resto de la API.

7. **Respuesta HTTP**  
   El controlador devuelve un `ResponseEntity` con el código HTTP adecuado (`200 OK`, `201 Created`, `202 Accepted`, `204 No Content`, `303 See Other`) y el cuerpo en JSON o XML.

#### Flujo de una petición asíncrona

Algunas operaciones del servicio no se resuelven de forma inmediata, sino que requieren la intervención de otro usuario. Es el caso de la **solicitud de seguimiento**.

1. **Creación de la tarea**  
   El seguidor envía una petición a `POST /api/v1/usuarios/{seguidorId}/seguidos` con el identificador del usuario al que quiere seguir. El controlador valida que ambos usuarios existen, que no se trata de seguirse a sí mismo y que no existe ya un seguimiento entre ellos. Si todo es correcto, el servicio crea una `TareaSeguimiento` con estado `PENDIENTE` y la almacena en memoria.

2. **Respuesta inmediata**  
   El controlador devuelve `202 Accepted` con la cabecera `Location` apuntando a la URI de la tarea (`/tareas/{taskId}`) y el cuerpo con la representación de la tarea. La operación **no se ha completado**, solo se ha aceptado para su procesamiento.

3. **Consulta de solicitudes pendientes**  
   El usuario seguido consulta sus solicitudes con `GET /api/v1/usuarios/{seguidoId}/solicitudes`. El servicio filtra las tareas almacenadas y devuelve solo aquellas dirigidas a él y que están en estado `PENDIENTE`, con enlaces HATEOAS al seguidor, a la tarea y a la operación de aceptar o rechazar.

4. **Aceptación o rechazo**  
   El usuario seguido envía `PUT /api/v1/usuarios/{seguidoId}/solicitudes/{seguidorId}` con el nuevo estado (`ACEPTADA` o `RECHAZADA`). El controlador valida que la tarea existe y que está en estado `PENDIENTE`. Si se acepta, se crea la relación de seguimiento real en la base de datos y la tarea pasa a `ACEPTADA`. Si se rechaza, la tarea pasa a `RECHAZADA`. En ambos casos se devuelve `204 No Content`.

5. **Consulta del estado de la tarea**  
   El seguidor puede consultar el estado con `GET /api/v1/tareas/{taskId}`. Si la tarea sigue `PENDIENTE` o ha sido `RECHAZADA`, se devuelve `200 OK` con la representación de la tarea. Si ya ha sido `ACEPTADA`, se devuelve `303 See Other` con la cabecera `Location` apuntando al recurso resultante (el listado de seguidos del seguidor).

#### Notas sobre la asincronía

- Las tareas se almacenan **en memoria** en un `ConcurrentHashMap`, por lo que **no persisten entre reinicios** de la aplicación.

- El proyecto tiene habilitado el soporte de asincronía de Spring mediante `@EnableAsync` en la clase principal.

- La operación no se ejecuta en un hilo aparte: el controlador crea la tarea y devuelve el `202 Accepted` inmediatamente, y es el cliente quien decide cuándo consultar el estado.

### Manejo de errores

Todas las excepciones personalizadas son capturadas de forma centralizada por clases anotadas con `@RestControllerAdvice`. Cada una traduce la excepción a un **código HTTP concreto** y devuelve un cuerpo `ErrorMessage` con un mensaje descriptivo generado en el constructor de la propia excepción.

A continuación se detallan todas las excepciones agrupadas por su `@RestControllerAdvice`.

#### `UsuarioExcepcionAdvice`

Gestiona todos los errores relacionados con usuarios, vinos de usuario y seguimientos.

| Excepción | Código HTTP | Descripción |
|---|---|---|
| `UsuarioNotFoundException` | `404 Not Found` | El usuario solicitado no existe en la base de datos. |
| `UsuarioMenorDeEdadException` | `400 Bad Request` | El usuario que se intenta registrar es menor de 18 años. |
| `UsuarioYaExisteException` | `400 Bad Request` | El correo electrónico ya está registrado por otro usuario. |
| `UsuarioVinoYaExisteException` | `400 Bad Request` | El vino ya forma parte de la lista del usuario. |
| `UsuarioVinoNotFoundException` | `400 Bad Request` | El vino indicado no pertenece a la lista del usuario. |
| `SeguimientoNotFoundException` | `400 Bad Request` | No existe una relación de seguimiento entre los usuarios indicados. |
| `SeguimientoYaExisteException` | `400 Bad Request` | Ya existe una relación de seguimiento entre los usuarios indicados. |
| `SeguimientoASiMismoException` | `400 Bad Request` | Un usuario intenta seguirse a sí mismo. |
| `SolicitudNoPendienteException` | `400 Bad Request` | La solicitud de seguimiento no está en estado `PENDIENTE` y no puede modificarse. |
| `SolicitudEstadoNoValidoActualizarException` | `400 Bad Request` | El estado proporcionado al actualizar la solicitud no es `ACEPTADA` ni `RECHAZADA`. |

#### `VinoExceptionAdvice`

| Excepción | Código HTTP | Descripción |
|---|---|---|
| `VinoNotFoundException` | `404 Not Found` | El vino solicitado no existe en la base de datos. |

#### `UvaExceptionAdvice`

| Excepción | Código HTTP | Descripción |
|---|---|---|
| `UvaNotFoundException` | `404 Not Found` | La uva solicitada no existe en la base de datos. |

#### `TareaExceptionAdvice`

| Excepción | Código HTTP | Descripción |
|---|---|---|
| `TareaNotFoundException` | `404 Not Found` | La tarea de seguimiento solicitada no existe. |

### Tecnologías

- Lenguaje: **Java**
- Framework: **Spring Boot**
- Implementación REST: **Spring Web MVC**
- Persistencia: **Spring Data JPA**
- Enlaces hipermedia en las respuestas: **Spring HATEOAS**
- Base de datos relacional: **MySQL**
- Despliegue y orquestación: **Docker** y **Docker Compose**
- Gestión de dependencias y ciclo de vida del proyecto: **Maven**

## Imagen de la aplicación

La imagen de la aplicación ([carlossanchezh/comunidadvinos-app](https://hub.docker.com/r/carlossanchezh/comunidadvinos-app)) está publicada en Docker Hub.

Consulta la sección de [Instalación y ejecución](#instalación-y-ejecución) para más detalles sobre el despliegue con Docker.

## Estructura del proyecto

```text
.
├── database/
│   └── comunidadvinos.sql                                       # Dump con estructura de la base de datos y datos de ejemplo
│
├── src/
│   ├── main/
│   │   ├── java/es/upm/sos/comunidadvinos/
│   │   │   ├── assembler/                                           # Assemblers HATEOAS
│   │   │   │   ├── SeguidoDataModelAssembler.java                   # Convierte Usuario en SeguidoData con enlace self
│   │   │   │   ├── SolicitudPendienteDataModelAssembler.java        # Convierte TareaSeguimiento en SolicitudPendienteData con enlaces a tarea, seguidor y aceptar/rechazar
│   │   │   │   ├── UsuarioModelAssembler.java                       # Convierte Usuario en su representación con enlace self
│   │   │   │   ├── UvaPorcentajeDataModelAssembler.java             # Convierte VinoUva en UvaPorcentajeData con enlace a la uva
│   │   │   │   └── VinoPuntuacionDataModelAssembler.java            # Convierte UsuarioVino en VinoPuntuacionData con enlace al vino
│   │   │   │
│   │   │   ├── controller/                                          # Endpoints REST
│   │   │   │   ├── TareasController.java                            # Endpoints de tareas, consulta el estado de una tarea asíncrona
│   │   │   │   ├── UsuarioController.java                           # Endpoints de usuarios, vinos de usuario, seguimientos, recomendaciones y estadísticas
│   │   │   │   ├── UvaController.java                               # Endpoints de uvas, obtiene una uva
│   │   │   │   └── VinoController.java                              # Endpoints de vinos, obtiene un vino
│   │   │   │
│   │   │   ├── exception/                                           # Excepciones y @RestControllerAdvice
│   │   │   │   ├── ErrorMessage.java                                # DTO con el mensaje de error devuelto al cliente
│   │   │   │   ├── SeguimientoASiMismoException.java                # Un usuario intenta seguirse a sí mismo
│   │   │   │   ├── SeguimientoDejarSeguirNoSigueException.java      # Se intenta dejar de seguir a alguien que no se sigue
│   │   │   │   ├── SeguimientoNotFoundException.java                # No existe la relación de seguimiento
│   │   │   │   ├── SeguimientoYaExisteException.java                # Ya existe la relación de seguimiento
│   │   │   │   ├── SolicitudEstadoNoValidoActualizarException.java  # Estado no válido al actualizar una solicitud
│   │   │   │   ├── SolicitudNoPendienteException.java               # La solicitud no está en estado PENDIENTE
│   │   │   │   ├── TareaExceptionAdvice.java                        # Manejador global de TareaNotFoundException
│   │   │   │   ├── TareaNotFoundException.java                      # La tarea de seguimiento no existe
│   │   │   │   ├── UsuarioExcepcionAdvice.java                      # Manejador global de errores de usuario, usuario-vino y seguimiento
│   │   │   │   ├── UsuarioMenorDeEdadException.java                 # El usuario que se intenta registrar es menor de 18
│   │   │   │   ├── UsuarioNotFoundException.java                    # El usuario solicitado no existe
│   │   │   │   ├── UsuarioVinoNotFoundException.java                # El vino no pertenece a la lista del usuario
│   │   │   │   ├── UsuarioVinoYaExisteException.java                # El vino ya está en la lista del usuario
│   │   │   │   ├── UsuarioYaExisteException.java                    # El correo ya está registrado
│   │   │   │   ├── UvaExceptionAdvice.java                          # Manejador global de UvaNotFoundException
│   │   │   │   ├── UvaNotFoundException.java                        # La uva solicitada no existe
│   │   │   │   ├── VinoExceptionAdvice.java                         # Manejador global de VinoNotFoundException
│   │   │   │   └── VinoNotFoundException.java                       # El vino solicitado no existe
│   │   │   │
│   │   │   ├── model/                                               # Entidades JPA, DTOs y claves compuestas
│   │   │   │   ├── EstadisticasData.java                            # DTO con la puntuación media del usuario
│   │   │   │   ├── EstadoTarea.java                                 # Enum: PENDIENTE, ACEPTADA, RECHAZADA
│   │   │   │   ├── RecomendacionesData.java                         # DTO con datos del usuario y sus listas de vinos recomendados
│   │   │   │   ├── SeguidoData.java                                 # DTO con datos básicos de un usuario seguido
│   │   │   │   ├── SeguidoRequestBody.java                          # DTO del cuerpo para solicitar un seguimiento
│   │   │   │   ├── Seguimiento.java                                 # Entidad de la relación usuario–usuario (seguidor, seguido)
│   │   │   │   ├── SeguimientoId.java                               # Clave compuesta (seguidorId, seguidoId)
│   │   │   │   ├── SolicitudPendienteData.java                      # DTO con los datos de una solicitud de seguimiento pendiente
│   │   │   │   ├── TareaSeguimiento.java                            # Entidad en memoria para las tareas asíncronas de seguimiento
│   │   │   │   ├── Usuario.java                                     # Entidad JPA del usuario
│   │   │   │   ├── UsuarioVino.java                                 # Entidad de la relación usuario–vino (puntuación, fecha)
│   │   │   │   ├── UsuarioVinoId.java                               # Clave compuesta (usuarioId, vinoId)
│   │   │   │   ├── UsuarioVinoRequestBody.java                      # DTO del cuerpo para añadir un vino a la lista de un usuario
│   │   │   │   ├── Uva.java                                         # Entidad JPA de la uva
│   │   │   │   ├── UvaPorcentajeData.java                           # DTO con datos de la uva y su porcentaje en el vino
│   │   │   │   ├── Vino.java                                        # Entidad JPA del vino
│   │   │   │   ├── VinoPuntuacionData.java                          # DTO con datos del vino y la puntuación del usuario
│   │   │   │   ├── VinoUva.java                                     # Entidad de la relación vino–uva (porcentaje)
│   │   │   │   └── VinoUvaId.java                                   # Clave compuesta (vinoId, uvaId)
│   │   │   │
│   │   │   ├── repository/                                          # Interfaces Spring Data JPA
│   │   │   │   ├── SeguimientoRepository.java                       # Consultas de seguimientos (existencia, listados, filtrado)
│   │   │   │   ├── UsuarioRepository.java                           # Consultas de usuarios (correo único, filtro por nombre, paginación)
│   │   │   │   ├── UsuarioVinoRepository.java                       # Consultas de usuario-vino con filtros JPQL complejos
│   │   │   │   ├── UvaRepository.java                               # Consultas de uvas
│   │   │   │   ├── VinoRepository.java                              # Consultas de vinos
│   │   │   │   └── VinoUvaRepository.java                           # Consultas de la relación vino-uva
│   │   │   │   
│   │   │   ├── service/                                             # Lógica de negocio
│   │   │   │   ├── SeguimientoService.java                          # Gestión de seguimientos persistidos en BD
│   │   │   │   ├── TareaSeguimientoService.java                     # Gestión de tareas asíncronas en memoria
│   │   │   │   ├── UsuarioService.java                              # Validaciones y CRUD de usuarios
│   │   │   │   ├── UsuarioVinoService.java                          # Gestión de la lista de vinos del usuario y filtros
│   │   │   │   ├── UvaService.java                                  # Consultas de uvas
│   │   │   │   ├── VinoService.java                                 # Consultas de vinos
│   │   │   │   └── VinoUvaService.java                              # Consultas de la composición de uvas de un vino
│   │   │   │ 
│   │   │   └── ComunidadvinosApplication.java                       # Punto de entrada Spring Boot
│   │   │
│   │   └── resources/
│   │       └── application.properties                               # Configuración de la aplicación
│   │ 
│   └── test/
│       └── java/es/upm/sos/comunidadvinos/
│           └── ComunidadvinosApplicationTests.java                  # Test de carga del contexto de Spring
│
├── .dockerignore                                                    # Archivos y carpetas excluidos del contexto de Docker
├── .gitignore                                                       # Archivos y carpetas ignorados por Git
├── Dockerfile                                                       # Imagen Docker de la aplicación
├── HELP.md                                                          # Documentación generada por Spring Initializr
├── INSTRUCTIONS.md                                                  # Instrucciones de instalación y ejecución del proyecto
├── README.md                                                        # Descripción del proyecto 
├── docker-compose.yml                                               # Orquestación de la app y MySQL
└── pom.xml                                                          # Configuración Maven: dependencias y plugins
```

## Instalación y ejecución

El proyecto puede ejecutarse de dos formas, de manera **Local** o utilizando **Docker**.

Consulta [INSTRUCTIONS.md](INSTRUCTIONS.md) para las instrucciones detalladas de instalación, ejecución y uso de la API.

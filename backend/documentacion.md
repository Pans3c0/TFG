# Memoria de Desarrollo - Proyecto TodoList (API REST)

**Autor:** [Samuel Edgar Pacheco Velin / Estudiante 2º DAW]

---

## 0. Planteamiento del Proyecto y Estado Inicial

Antes de comenzar con el desarrollo iterativo de las fases, realicé un análisis del proyecto base proporcionado.
El estado inicial del código ya contaba con:

1. **Arquitectura MVC Básica:** Controladores, Servicios, Repositorios, Entidades y DTOs estructurados correctamente.
2. **Entidades Fundamentales:** Las clases `User`, `Task`, `Category` y `Tag` ya estaban definidas con relaciones como `@ManyToOne` y `@ManyToMany`.
3. **Autenticación (Hardcoded):** Un `AuthController` con un registro y login básicos, pero validando contraseñas en texto plano.
4. **Dependencias Preparadas:** `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, lombok, y el conector de base de datos.
5. **Configuración de Swagger:** La dependencia `springdoc-openapi` ya estaba en el `pom.xml`, lista para ser aprovechada.

Mi plan de trabajo se dividió en 4 fases para abordar los requisitos del enunciado de manera estructurada:

- **Fase 1:** Ampliación del modelo y completar los CRUDs faltantes.
- **Fase 2:** Implementación de la lógica de negocio, endpoints de Admin y estadísticas (Dashboard).
- **Fase 3:** Integración de Spring Security y JWT.
- **Fase 4:** Documentación formal de la API con OpenAPI/Swagger.

**Nota sobre la nomenclatura del proyecto:** Las entidades principales se nombran en español según el contexto educativo español:

- `Usuario` (en lugar de `User`): Entidad que representa al usuario del sistema.
- `Tarea` (en lugar de `Task`): Entidad que representa una tarea o item en la lista de tareas.
- `Etiqueta` (en lugar de `Tag`): Entidad que representa una etiqueta/clasificación.
- `Categoria` (en lugar de `Category`): Entidad que representa una categoría de tareas.

---

**Fase 1:** Modelado de Datos y Operaciones CRUD Básicas

## 1. Ampliación de la Entidad `Tarea` (Task)

El enunciado pedía añadir al menos dos elementos nuevos a la entidad `Tarea` para personalizar la aplicación.
En mi caso, he decidido añadir dos campos fundamentales para gestionar mejor el flujo de trabajo:

- **`prioridad` (Priority):** Usando un Enum `TaskPriority` con los valores `ALTA`, `MEDIA`, `BAJA`.
- **`estado` (Status):** Usando un Enum `TaskStatus` con los valores `PENDIENTE`, `EN_PROGRESO`, `COMPLETADA_PARCIALMENTE`, `COMPLETADA`.

Además, la entidad `Tarea` cuenta con otros campos adicionales:
- **`titulo`:** El nombre o título de la tarea.
- **`descripcion`:** Descripción detallada de la tarea.
- **`completada`:** Booleano que indica si la tarea ha sido completada.
- **`fechaCreacion`:** Fecha y hora de creación de la tarea (se establece automáticamente).
- **`fechaLimite`:** Fecha límite (deadline) para completar la tarea.
- **`autor`:** Relación `@ManyToOne` con la entidad `Usuario` que indica quién creó la tarea.
- **`etiquetas`:** Relación `@ManyToMany` con la entidad `Etiqueta` para clasificar tareas.
- **`categoria`:** Relación `@ManyToOne` con la entidad `Categoria` para agrupar por categoría.

**Decisión técnica:**
He utilizado la anotación `@Enumerated(EnumType.STRING)` en la entidad `Tarea` para los campos `prioridad` y `estado`. Esto es importante porque si no lo pongo, JPA guardaría el Enum como un número (0, 1, 2...), lo cual puede dar problemas en el futuro si cambio el orden de los enums en el código. Al guardarlo como texto (STRING), la base de datos almacena directamente "ALTA" o "PENDIENTE", lo que hace que los datos sean legibles directamente mirando la tabla.

## 2. Implementación de DTOs (Data Transfer Objects)

Como buen desarrollador Junior, he comprendido que **nunca debo devolver las entidades directamente al frontend**, especialmente cuando hay contraseñas de por medio.

- **UsuarioResponse, CreateUserRequest y ActualizarUsuarioRequest:** He añadido el campo `nombreCompleto` (que estaba en el diagrama UML). He creado un `ActualizarUsuarioRequest` para permitir actualizar al usuario. Al devolver la información, uso un `UsuarioResponse` que **no incluye** la contraseña, por razones de seguridad.
- **TareaResponse y TareaRequest:** DTOs para la gestión de tareas. `TareaRequest` incluye `titulo`, `descripcion`, `fechaLimite`, `prioridad`, `estado` y `categoriaId`. `TareaResponse` devuelve todos estos datos incluyendo la `prioridad` y el `estado`.
- **CategoriaResponse y CategoriaRequest:** DTOs para la gestión de categorías.
- **EtiquetaResponse y EtiquetaRequest:** DTOs para la gestión de etiquetas.
- **DashboardResponse:** DTO especial que devuelve las estadísticas del dashboard (total, completadas, pendientes).
- **AuthResponse:** DTO que devuelve la información del usuario y el **Token JWT** tras el login o registro.

## 3. Completando los Controladores y Servicios (CRUD)

Para tener la API 100% funcional a nivel básico, me he asegurado de tener todas las operaciones CRUD (Crear, Leer, Actualizar, Borrar) de los modelos de datos.

### 3.1. Gestión de Etiquetas (Etiqueta)

He creado desde cero el `EtiquetaController` y `EtiquetaService`. El controlador expone la ruta `/etiquetas` y llama al servicio.
La entidad `Etiqueta` tiene un campo `nombre` que se utiliza para clasificar tareas. Se puede crear, listar, actualizar y eliminar etiquetas a través de los endpoints correspondientes.

### 3.2. Gestión de Usuarios

A partir de la autenticación básica que ya tenía en `AuthController`, he construido un `UsuarioController` completo que permite listar usuarios (`GET /usuarios`), buscar uno por ID, actualizar sus datos (`PUT /usuarios/{id}`) y eliminar la cuenta.

### 3.3. Gestión de Categorías

He creado el `CategoriaController` y `CategoriaService` que permiten gestionar categorías de tareas. Las categorías pueden ser creadas, listadas, actualizadas y eliminadas únicamente por usuarios con rol `ADMIN` o `GESTOR`.

### 3.4. Refactorización: Añadiendo métodos PUT (Actualizar)

Me aseguré de que `TareaController` y `CategoriaController` tengan métodos para modificar registros existentes.
He utilizado la anotación `@PutMapping("/{id}")` que recoge el ID de la URL usando `@PathVariable` y los nuevos datos mediante `@RequestBody`.
En la capa de servicio (`TareaService` y `CategoriaService`), busco el objeto por ID, compruebo que exista con `.orElseThrow()`, lo modifico con los `setters` y lo guardo con `repository.save()`.

---

**Fase 2:** Lógica Avanzada y Endpoints de Negocio

## 4. Endpoints de Administrador (Promoción y Degradación)

Para cumplir con los requisitos del administrador, he implementado dos nuevos endpoints en `UserController`:

- `PUT /users/{id}/promote`: Cambia el rol de un usuario a `GESTOR`.
- `PUT /users/{id}/demote`: Cambia el rol de un usuario a `USER`.
  Técnicamente, el servicio busca al usuario por su ID, modifica el atributo `rol` y lo guarda. Cuando implementemos la seguridad en la siguiente fase, me aseguraré de que estos endpoints solo puedan ser llamados por alguien con rol `ADMIN`.

## 5. Gestión de Etiquetas en Tareas

Las relaciones `ManyToMany` (Muchos a Muchos) entre `Task` y `Tag` ya estaban definidas, pero faltaba la forma de asignar y quitar etiquetas a través de la API. He añadido dos endpoints en `TaskController`:

- `POST /tasks/{id}/tags/{tagId}`: Añade la etiqueta a la tarea.
- `DELETE /tasks/{id}/tags/{tagId}`: Quita la etiqueta de la tarea.
  La lógica en `TaskService` usa `task.getTags().add(tag)` y `task.getTags().remove(tag)`. Al guardar la tarea, Hibernate se encarga de actualizar la tabla intermedia `task_tags` automáticamente.

## 6. Búsquedas Complejas

El enunciado pedía realizar búsquedas por los campos nuevos añadidos. Como añadimos `priority` y la relación con `tags`, he creado métodos personalizados en `TaskRepository`:

- `List<Task> findByPriority(TaskPriority priority)`
- `List<Task> findByTagsId(Long id)`

Spring Data JPA genera automáticamente las sentencias SQL para estas consultas solo con nombrarlas bien. Estos métodos están expuestos como `GET /tasks/search/priority` y `GET /tasks/search/tag`.

## 7. Endpoint Dashboard (Estadísticas)

Para dar un valor añadido y cumplir con la visualización de la información, he creado un `DashboardController` (`/dashboard`). Devuelve un DTO llamado `DashboardResponse` con tres valores:

- Total de tareas en el sistema.
- Tareas completadas.
- Tareas pendientes.
  He utilizado métodos agregados en `TaskRepository` como `count()`, `countByCompletedTrue()` y `countByCompletedFalse()`. Usar métodos `count` en JPA es mucho más eficiente que descargar todas las tareas y contarlas en memoria, porque la base de datos hace el conteo nativo y devuelve solo un número.

---

**Fase 3:** Seguridad con Spring Security y JWT

## 8. Configuración de Spring Security y JWT

He implementado una arquitectura de seguridad Stateless (sin estado) utilizando JSON Web Tokens (JWT) y Spring Security. Esta era una de las partes más críticas del sistema:

1. **Dependencias:** Se añadió `spring-boot-starter-security` y la librería `jjwt` (api, impl y jackson) al `pom.xml`.
2. **UserDetails:** La clase `User` ahora implementa la interfaz `UserDetails` de Spring Security, mapeando el rol interno a los roles de Spring (e.g. `ROLE_USER`, `ROLE_ADMIN`).
3. **JwtService:** Se creó un servicio para generar, desencriptar y validar tokens JWT firmados con el algoritmo HS256 y una clave secreta fuerte. Los tokens expiran en 24 horas.
4. **Filtro Personalizado (JwtAuthenticationFilter):** Se construyó un filtro que intercepta todas las peticiones, lee el header `Authorization`, verifica si hay un token `Bearer`, y si es válido, inyecta al usuario en el `SecurityContextHolder`.
5. **Configuración de Rutas (SecurityConfig):**
   - Las rutas `/auth/**` (login y registro) y las de swagger están abiertas (`permitAll()`).
   - Los endpoints de promoción/degradación (`/users/*/promote`) están limitados a `ADMIN`.
   - Las categorías (`/categories/**`) están limitadas a `ADMIN` y `GESTOR`.
   - El resto de la API requiere autenticación general.

## 9. Refactorización de Autenticación

El registro y el login se han refactorizado para soportar este nuevo modelo:

- **Encriptación:** Al registrar o actualizar contraseñas, ahora se pasan por `BCryptPasswordEncoder` para asegurar que nunca se almacenen en texto plano.
- **Login Manager:** El endpoint `/auth/login` ahora delega la validación de credenciales al `AuthenticationManager` de Spring.
- **AuthResponse:** En lugar de devolver solo el usuario, el registro y el login ahora devuelven un DTO especial `AuthResponse` que incluye tanto la información del usuario como el **Token JWT** necesario para futuras peticiones.

---

**Fase 4:** Documentación Final de la API (Swagger)

## 10. Swagger / OpenAPI

Para cumplir con los últimos requisitos del proyecto, he documentado exhaustivamente todos los endpoints de la API.

- Se han utilizado las anotaciones `@Tag` a nivel de clase en todos los controladores para agrupar los endpoints por dominio (Users, Tasks, Categories, Tags, Auth, Dashboard).
- Se ha aplicado la anotación `@Operation(summary = "...", description = "...")` en cada método de los controladores para explicar claramente el propósito y comportamiento de cada endpoint.
- Se ha utilizado `@Parameter(description = "...")` para aclarar el propósito de las variables de ruta (`@PathVariable`) y parámetros de consulta (`@RequestParam`).

Con esto, la interfaz gráfica autogenerada de Swagger UI (accesible en `/swagger-ui/index.html`) se convierte en una herramienta viva y completamente explicativa que permite probar la API en tiempo real.

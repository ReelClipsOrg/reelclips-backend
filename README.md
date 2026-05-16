# ReelClips - Plataforma de videos cortos e interacciones

Proyecto desarrollado para la asignatura **Diseño y Arquitectura de Software** de la **Universidad de La Sabana - Facultad de Ingeniería**.

ReelClips es una plataforma enfocada en el consumo, creación y compartición de videos cortos. Permite interacción social mediante likes, comentarios, categorías, feed dinámico y chat en tiempo real entre usuarios.

---

## Equipo de trabajo

| Integrante                    | Rol                  |
|-------------------------------|----------------------|
| Nicolas Joel Cáceres Parra    | Desarrollo Frontend  |
| Jorge Luis Alarcón Isturiz    | Desarrollo Backend   |
| Daniel Felipe Esquinas Suarez | Desarrollo Backend   |
| Eduard Meza Salazar           | Desarrollo Backend   |
| Juan José Campos Covaleda     | Desarrollo Backend   |

**Docente:** Wilmer Fabian Triana Pulgarín  
**Curso:** Diseño y Arquitectura de Software  
**Semestre:** 2026-1

---

## Objetivo del proyecto

Desarrollar un sistema de información que permita a los usuarios consumir, crear y compartir videos cortos, con el fin de ofrecer una plataforma enfocada en la libre expresión digital, la interacción social y el descubrimiento de contenido dinámico, accesible y centrado en la experiencia del usuario.

---

## Requerimientos funcionales

### Gestión de usuarios

| ID    | Requerimiento                                                                                           |
|-------|---------------------------------------------------------------------------------------------------------|
| RF-01 | El sistema debe permitir a un usuario registrarse proporcionando nombre de usuario, correo y contraseña. |
| RF-02 | El sistema debe permitir iniciar sesión mediante credenciales.                                           |
| RF-03 | El usuario debe poder cerrar sesión en cualquier momento.                                                |
| RF-04 | El usuario debe poder editar su perfil, foto y descripción.                                             |
| RF-05 | El sistema debe permitir visualizar perfiles de usuarios.                                               |
| RF-06 | El usuario debe poder desactivar su cuenta.                                                             |

### Gestión de reels

| ID    | Requerimiento                                                        |
|-------|----------------------------------------------------------------------|
| RF-07 | El sistema debe permitir publicar reels con descripción y categoría. |
| RF-08 | El usuario debe poder editar descripción y categorías de sus reels.  |
| RF-09 | El usuario debe poder eliminar sus reels.                            |
| RF-10 | El sistema debe permitir visualizar reels publicados.                |

### Interacciones sociales

| ID    | Requerimiento                                                      |
|-------|--------------------------------------------------------------------|
| RF-12 | El sistema debe permitir reaccionar con likes a un reel.           |
| RF-13 | El usuario debe poder eliminar una reacción previamente realizada. |
| RF-14 | El sistema debe permitir comentar reels.                           |
| RF-15 | El usuario debe poder eliminar sus comentarios.                    |

### Sistema de chat

| ID    | Requerimiento                                                     |
|-------|-------------------------------------------------------------------|
| RF-16 | El sistema debe permitir iniciar conversaciones entre usuarios.   |
| RF-17 | El sistema debe permitir enviar mensajes de texto entre usuarios. |

### Gestión de categorías

| ID    | Requerimiento                                                                 |
|-------|-------------------------------------------------------------------------------|
| RF-18 | El sistema debe obligar a asignar al menos una categoría al publicar un reel. |
| RF-19 | El sistema debe permitir administrar las categorías disponibles.              |

### Feed de contenido

| ID    | Requerimiento                                                            |
|-------|--------------------------------------------------------------------------|
| RF-20 | El sistema debe mostrar un feed de reels públicos.                       |
| RF-21 | El usuario debe poder filtrar contenido por categorías.                  |
| RF-22 | El sistema debe cargar contenido continuamente mediante scroll infinito. |

---

## Reglas de negocio

### Cuentas de usuario

| ID    | Regla                                                                                                 |
|-------|-------------------------------------------------------------------------------------------------------|
| RN-01 | Cada usuario debe tener un identificador único dentro de la plataforma.                               |
| RN-02 | Solo usuarios autenticados pueden publicar, reaccionar, comentar o iniciar chats.                     |
| RN-03 | Cada cuenta crea automáticamente un canal personal único.                                             |
| RN-04 | El nombre de usuario solo puede modificarse una vez cada 30 días.                                    |
| RN-05 | Una cuenta desactivada conserva reels y mensajes durante 30 días antes de eliminarse permanentemente. |

### Publicación de reels

| ID    | Regla                                                        |
|-------|--------------------------------------------------------------|
| RN-06 | Los reels deben tener máximo 90 segundos y 500 MB de tamaño. |
| RN-07 | Todo reel debe tener al menos una categoría asignada.        |
| RN-08 | Solo el propietario del reel puede editarlo o eliminarlo.    |
| RN-09 | Todo reel publicado es público por defecto.                  |

### Feed de contenido

| ID    | Regla                                                                               |
|-------|-------------------------------------------------------------------------------------|
| RN-10 | El feed principal muestra contenido de toda la comunidad.                           |
| RN-11 | El sistema organiza y filtra contenido según categorías e historial de interacción. |
| RN-12 | Los reels propios no aparecen en el feed de descubrimiento del usuario.             |
| RN-13 | Cada usuario puede dejar únicamente una reacción por reel.                          |
| RN-14 | El feed debe precargar contenido para soportar scroll infinito.                     |

### Chat directo

| ID    | Regla                                                                                           |
|-------|-------------------------------------------------------------------------------------------------|
| RN-15 | Solo usuarios registrados pueden participar en chats.                                           |
| RN-16 | Las conversaciones son exclusivamente uno a uno.                                                |
| RN-17 | Cualquier usuario autenticado puede iniciar conversaciones.                                     |
| RN-18 | Los mensajes permanecen almacenados persistentemente.                                           |
| RN-19 | Los mensajes pueden contener texto y referencias a reels, pero no archivos multimedia directos. |

---

## Arquitectura

El sistema adopta una arquitectura de **Monolito Modular**, organizada internamente por módulos independientes. Cada módulo expone contratos públicos en paquetes `api/` y encapsula su implementación en paquetes `internal/`.

### Módulos principales

| Módulo        | Responsabilidad                                     |
|---------------|-----------------------------------------------------|
| Usuarios      | Registro, autenticación y gestión de perfiles       |
| Reels         | Publicación, edición, streaming y consulta de videos |
| Categorías    | Gestión y filtrado de categorías temáticas          |
| Feed          | Filtrado, paginación y personalización de contenido |
| Interacciones | Likes, comentarios y eventos sociales               |
| Chat          | Conversaciones y mensajes directos en tiempo real   |
| Shared        | Configuración, enums, excepciones y almacenamiento  |

---

## Patrones de diseño aplicados

| Patrón   | Categoría      | Ubicación                                      | Propósito                                                               |
|----------|----------------|------------------------------------------------|-------------------------------------------------------------------------|
| Facade   | Estructural    | `feed/FacadeFeed.java`                         | Centraliza y simplifica la orquestación del feed                        |
| Proxy    | Estructural    | `reels/internal/proxy/ProxyReel.java`          | Controla el acceso y la caché de streams de video                       |
| Observer | Comportamiento | `interacciones/internal/observer/`             | Distribuye eventos de interacción a múltiples observadores desacoplados |

---

## Atributos de calidad

### Rendimiento

El rendimiento es el atributo central del sistema. En una plataforma de videos cortos, la experiencia del usuario depende directamente de la velocidad de carga y reproducción.

| Métrica                        | Valor objetivo                  |
|-------------------------------|---------------------------------|
| Tiempo de carga inicial        | <= 2 segundos en el 95% de casos |
| Latencia en cambio de video    | <= 1 segundo                    |
| Tiempo de carga de comentarios | <= 1.5 segundos                 |

### Otros atributos

| Atributo       | Descripción                                                                |
|----------------|----------------------------------------------------------------------------|
| Escalabilidad  | Soportar crecimiento de 1.000 a 100.000 usuarios sin degradación crítica   |
| Usabilidad     | Tiempo de aprendizaje inicial <= 3 minutos, tasa de abandono < 30%         |
| Disponibilidad | >= 99% mensual, recuperación ante fallos <= 5 minutos en servicios críticos |

---

## Stack tecnológico

| Capa                 | Tecnología                                  |
|----------------------|---------------------------------------------|
| Backend              | Spring Boot 4.0.6 + Java 17                 |
| API REST             | Spring Web MVC                              |
| Persistencia         | Spring Data JPA + Hibernate                 |
| Base de datos        | PostgreSQL                                  |
| WebSocket            | STOMP sobre SockJS y WebSocket nativo       |
| Documentación API    | Springdoc OpenAPI + Swagger UI              |
| Almacenamiento       | Supabase Storage                            |
| Build                | Maven Wrapper                               |
| Contenedores locales | Docker Compose para backend + PostgreSQL    |

> Este repositorio contiene el backend. No incluye el proyecto frontend.

---

## Estructura del proyecto

```text
src/main/java/org/arquitectura/reelclipsv2/
├── ReelclipsV2Application.java
├── usuarios/
│   ├── UsuariosModulo.java
│   ├── api/
│   │   ├── IUsuarioModuloApi.java
│   │   └── dto/
│   │       ├── PerfilInfo.java
│   │       └── UsuarioInfo.java
│   └── internal/
│       ├── controller/
│       │   └── UsuarioController.java
│       ├── model/
│       │   ├── Canal.java
│       │   └── Usuario.java
│       ├── repository/
│       │   ├── ICanalRepository.java
│       │   └── IUsuarioRepository.java
│       └── service/
│           └── UsuarioService.java
├── reels/
│   ├── ReelsModulo.java
│   ├── api/
│   │   ├── IReelModuloApi.java
│   │   └── dto/
│   │       └── ReelInfo.java
│   └── internal/
│       ├── controller/
│       │   └── ReelController.java
│       ├── model/
│       │   └── Reel.java
│       ├── proxy/
│       │   ├── CacheVideo.java
│       │   ├── ProxyReel.java
│       │   ├── ServicioAlmacenamientoVideo.java
│       │   ├── ServicioAutorizacion.java
│       │   └── VideoStream.java
│       ├── repository/
│       │   └── IReelRepository.java
│       └── service/
│           └── ReelService.java
├── categorias/
│   ├── CategoriasModulo.java
│   ├── api/
│   │   ├── ICategoriaModuloApi.java
│   │   └── dto/
│   │       └── CategoriaInfo.java
│   └── internal/
│       ├── controller/
│       │   └── CategoriaController.java
│       ├── model/
│       │   └── Categoria.java
│       ├── repository/
│       │   └── ICategoriaRepository.java
│       └── service/
│           ├── ServicioAdminCategorias.java
│           └── ServicioFiltroCategorias.java
├── feed/
│   ├── FacadeFeed.java
│   ├── api/
│   │   └── dto/
│   │       └── FeedResponse.java
│   └── internal/
│       ├── controller/
│       │   └── FeedController.java
│       └── service/
│           ├── ServicioFiltroVisibilidad.java
│           └── ServicioPaginacion.java
├── interacciones/
│   ├── InteraccionesModulo.java
│   ├── api/
│   │   └── dto/
│   │       └── InteraccionInfo.java
│   └── internal/
│       ├── controller/
│       │   └── InteraccionController.java
│       ├── model/
│       │   ├── Comentario.java
│       │   ├── EventoInteraccion.java
│       │   └── Reaccion.java
│       ├── observer/
│       │   ├── ActualizadorMetricas.java
│       │   ├── AnalizadorActividad.java
│       │   ├── IObservador.java
│       │   ├── NotificadorAutor.java
│       │   └── PublicadorEventosInteraccion.java
│       ├── repository/
│       │   ├── IComentarioRepository.java
│       │   └── IReaccionRepository.java
│       └── service/
│           └── InteraccionService.java
├── chat/
│   ├── ChatModulo.java
│   ├── api/
│   │   └── dto/
│   │       ├── ConversacionInfo.java
│   │       └── MensajeInfo.java
│   └── internal/
│       ├── config/
│       │   └── WebSocketConfig.java
│       ├── controller/
│       │   └── ChatController.java
│       ├── model/
│       │   ├── Conversacion.java
│       │   ├── Mensaje.java
│       │   └── ParticipanteConversacion.java
│       ├── repository/
│       │   ├── IConversacionRepository.java
│       │   └── IMensajeRepository.java
│       ├── service/
│       │   └── ChatService.java
│       └── websocket/
│           ├── ChatWebSocketController.java
│           ├── EscribiendoWS.java
│           ├── MensajeEntranteWS.java
│           └── MensajeSalienteWS.java
└── shared/
    ├── config/
    │   └── SwaggerConfig.java
    ├── enums/
    │   ├── EstadoCuenta.java
    │   ├── EstadoReel.java
    │   └── TipoMensaje.java
    ├── exception/
    │   ├── AccesoDenegadoException.java
    │   ├── GlobalExceptionHandler.java
    │   ├── RecursoNoEncontradoException.java
    │   └── ReglaNegocioException.java
    └── storage/
        └── SupabaseStorageService.java
```

---

## Endpoints REST

| Módulo        | Método | Endpoint                                      | RF        |
|---------------|--------|-----------------------------------------------|-----------|
| Usuarios      | POST   | `/api/usuarios/registro`                      | RF-01     |
| Usuarios      | POST   | `/api/usuarios/login`                         | RF-02     |
| Usuarios      | GET    | `/api/usuarios/{id}/perfil`                   | RF-05     |
| Usuarios      | PUT    | `/api/usuarios/{id}/perfil`                   | RF-04     |
| Usuarios      | POST   | `/api/usuarios/{id}/foto`                     | RF-04     |
| Usuarios      | PATCH  | `/api/usuarios/{id}/username`                 | RF-04     |
| Usuarios      | DELETE | `/api/usuarios/{id}`                          | RF-06     |
| Reels         | POST   | `/api/reels`                                  | RF-07     |
| Reels         | GET    | `/api/reels`                                  | RF-10     |
| Reels         | GET    | `/api/reels/{id}`                             | RF-10     |
| Reels         | PUT    | `/api/reels/{reelId}`                         | RF-08     |
| Reels         | DELETE | `/api/reels/{reelId}`                         | RF-09     |
| Reels         | GET    | `/api/reels/{reelId}/stream`                  | RF-10     |
| Reels         | GET    | `/api/reels/canal/{canalId}`                  | RF-10     |
| Categorías    | GET    | `/api/categorias`                             | RF-19     |
| Categorías    | GET    | `/api/categorias/{id}`                        | RF-19     |
| Categorías    | POST   | `/api/categorias`                             | RF-19     |
| Categorías    | PUT    | `/api/categorias/{id}`                        | RF-19     |
| Categorías    | DELETE | `/api/categorias/{id}`                        | RF-19     |
| Categorías    | GET    | `/api/categorias/filtrar`                     | RF-21     |
| Interacciones | POST   | `/api/interacciones/like`                     | RF-12     |
| Interacciones | DELETE | `/api/interacciones/like`                     | RF-13     |
| Interacciones | POST   | `/api/interacciones/comentario`               | RF-14     |
| Interacciones | DELETE | `/api/interacciones/comentario/{comentarioId}` | RF-15    |
| Interacciones | GET    | `/api/interacciones/comentarios/{reelId}`     | RF-14     |
| Feed          | GET    | `/api/feed`                                   | RF-20,21,22 |
| Chat          | POST   | `/api/chat/conversacion`                      | RF-16     |
| Chat          | GET    | `/api/chat/conversacion/{conversacionId}/mensajes` | RF-17 |

La documentación interactiva está disponible en:

```text
http://localhost:8082/swagger-ui.html
```

---

## WebSocket

| Canal                  | Dirección          | Propósito                        |
|------------------------|--------------------|----------------------------------|
| `/ws-chat`             | Conexión           | Endpoint STOMP usando SockJS     |
| `/ws-chat-native`      | Conexión           | Endpoint STOMP sin usar SockJS   |
| `/app/chat.enviar`     | Cliente -> Servidor | Enviar un mensaje                |
| `/app/chat.escribiendo` | Cliente -> Servidor | Notificar que un usuario escribe |
| `/user/queue/mensajes` | Servidor -> Cliente | Recibir mensajes nuevos          |
| `/user/queue/escribiendo` | Servidor -> Cliente | Recibir estado de escritura      |
| `/user/queue/errores`  | Servidor -> Cliente | Recibir errores del chat         |

El usuario se identifica durante el handshake con el parámetro `usuarioId`:

```text
/ws-chat?usuarioId=1
/ws-chat-native?usuarioId=1
```

---

## Configuración

El archivo principal de configuración está en:

```text
src/main/resources/application.properties
```

Variables principales:

```properties
server.port=8082

spring.datasource.url=jdbc:postgresql://localhost:5432/reelclips_db
spring.datasource.username=postgres
spring.datasource.password=reelclips123
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update

supabase.url=https://...
supabase.key=...
supabase.bucket.reels=reels
supabase.bucket.imagenes=imagenes-perfil
```

---

## Ejecución del proyecto

### Requisitos previos

- Java 17 o superior
- Maven 3.8+ o Maven Wrapper incluido
- PostgreSQL 14+ o Docker
- Cuenta y buckets configurados en Supabase Storage

### Levantar la aplicación completa con Docker

```bash
docker compose up -d
```

Este comando construye y levanta:

- `reelclips_app`: backend Spring Boot en `http://localhost:8082`
- `reelclips_db`: PostgreSQL en `localhost:5432`

Para reconstruir la imagen después de cambios en el código:

```bash
docker compose up -d --build
```

Para ver logs:

```bash
docker compose logs -f app
```

Para detener los servicios:

```bash
docker compose down
```

El volumen `reelclips_data` conserva los datos de PostgreSQL entre reinicios.

### Ejecutar el backend sin Docker

En Windows:

```bash
./mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
./mvnw spring-boot:run
```

La aplicación queda disponible en:

```text
http://localhost:8082
```

Si el puerto `8082` ya está ocupado, se puede publicar la app en otro puerto:

```bash
APP_PORT=8083 docker compose up -d
```

En PowerShell:

```powershell
$env:APP_PORT="8083"
docker compose up -d
```

### Ejecutar pruebas

```bash
./mvnw test
```

Actualmente el proyecto incluye una prueba base de carga del contexto de Spring Boot.

---

## Base de datos

Crear la base de datos si no se usa Docker:

```sql
CREATE DATABASE reelclips_db;
```

Hibernate está configurado con:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Por tanto, las tablas se crean o actualizan automáticamente al iniciar la aplicación, siempre que PostgreSQL esté disponible y las credenciales sean correctas.

---

## Estado del proyecto

ReelClips se encuentra actualmente en **fase de desarrollo**.

El proyecto cuenta con:

- [x] Objetivo del sistema
- [x] Requerimientos funcionales y reglas de negocio
- [x] Atributos de calidad definidos
- [x] Estilo arquitectónico definido: Monolito Modular
- [x] Stack tecnológico definido
- [x] Estructura modular backend implementada
- [x] Módulos de usuarios, reels, categorías, feed, interacciones y chat
- [x] Chat en tiempo real con WebSocket/STOMP
- [x] Documentación Swagger/OpenAPI
- [x] Integración con Supabase Storage
- [x] Docker Compose para PostgreSQL local
- [ ] Frontend integrado en este repositorio
- [ ] Pruebas de integración completas
- [ ] Despliegue en producción

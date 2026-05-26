# Glosario de Dominio — ReelClips
 
## Introducción
 
Este glosario define los principales términos y conceptos utilizados dentro del dominio de ReelClips. Su objetivo es unificar el lenguaje utilizado en el desarrollo, la documentación y la arquitectura del sistema.
 
---
 
# Términos del Dominio
 
- **Usuario:** Persona registrada en la plataforma. Puede consumir contenido, publicar reels, reaccionar, comentar e iniciar chats si tiene sesión activa.

- **Usuario autenticado:** Usuario que inició sesión correctamente y puede ejecutar acciones privadas.
 
- **Usuario no autenticado:** Usuario que solo puede ver contenido público y no puede interactuar con funciones privadas.
 
- **Canal:** Espacio personal asociado a cada cuenta. Representa el perfil y el contenido publicado por el usuario.
 
- **Reel:** Video corto publicado en la plataforma con descripción, categoría y visibilidad pública por defecto.
 
- **Feed:** Lista de reels que el usuario consume en pantalla. Puede filtrarse por categorías y cargarse de forma continua mediante scroll infinito.
 
- **Categoría:** Etiqueta temática asociada a un reel. Sirve para organizar, filtrar y descubrir contenido.
 
- **Reacción:** Interacción de tipo “like” sobre un reel. Cada usuario puede tener una sola reacción por reel.
 
- **Comentario:** Mensaje público escrito sobre un reel.
 
- **Conversación:** Hilo de chat entre exactamente dos usuarios.
 
- **Mensaje:** Unidad de comunicación dentro de una conversación. Puede contener texto y referencias a reels.
 
- **Visibilidad:** Regla que define si un reel puede ser visto por otros usuarios en el feed.
 
- **Scroll infinito:** Mecanismo de carga continua del feed sin cambiar de página manualmente.
 
---
 
# Patrones y Conceptos Arquitectónicos
 
- **Facade:** Patrón de diseño que centraliza la coordinación de un caso de uso complejo. En ReelClips se usa en el feed y en la exposición pública de módulos.
 
- **Proxy:** Patrón de diseño que controla y optimiza el acceso a un recurso. En ReelClips se usa para la entrega de reels y su caché.
 
- **Observer:** Patrón de diseño que notifica a varios componentes cuando ocurre un evento. En ReelClips se usa para eventos de interacciones.
 
- **DTO (Data Transfer Object):** Objeto de transferencia de datos utilizado para exponer información sin revelar la estructura interna de las entidades.
 
- **Repositorio:** Capa encargada del acceso y persistencia de datos.
 
- **Monolito modular:** Arquitectura donde toda la aplicación se despliega como una sola unidad, pero el código se divide en módulos con responsabilidades claras.
 
---
 
# Tecnologías del Sistema
 
- **Supabase Storage:** Servicio externo utilizado para almacenar archivos pesados como videos e imágenes.
 
- **PostgreSQL:** Base de datos relacional principal del proyecto.
 
- **Redis:** Almacén en memoria utilizado para acelerar accesos frecuentes y mejorar el rendimiento.

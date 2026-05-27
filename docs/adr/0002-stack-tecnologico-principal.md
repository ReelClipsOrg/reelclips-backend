# ADR-0002: Seleccionar el stack tecnológico principal

- **Status:** Accepted
- **Fecha:** 2026-05-26
- **Autores:** Equipo ReelClips

---

## Contexto

El sistema necesita una interfaz moderna para consumo de contenido multimedia, un backend robusto para reglas de negocio, almacenamiento persistente de información relacional y soporte para archivos multimedia de gran tamaño. También se requiere optimizar el rendimiento del feed y de consultas frecuentes.

---

## Decisión

Se adopta el siguiente stack tecnológico:

- **Frontend:** Next.js + TypeScript
- **Backend:** Spring Boot + Java 17
- **Base de datos:** PostgreSQL
- **Caché:** Redis
- **Almacenamiento multimedia:** Supabase Storage

---

## Opciones evaluadas

| Opción                    | Ventajas                                      | Desventajas                                   | Decisión   |
|---------------------------|-----------------------------------------------|-----------------------------------------------|------------|
| **Spring Boot + Next.js** | Madurez, buen soporte y experiencia del equipo | -                                             | **Aceptada** |
| NestJS + React            | Buena estructura y TypeScript nativo          | Menor experiencia del equipo                  | Rechazada  |
| Django + React            | Desarrollo rápido                             | Menor rendimiento esperado                    | Rechazada  |

---

## Consecuencias

### Positivas

- Next.js facilita una experiencia rápida y moderna.
- Spring Boot simplifica la creación de APIs REST.
- PostgreSQL soporta adecuadamente relaciones complejas.
- Redis mejora tiempos de respuesta del feed.
- Supabase desacopla multimedia de la base transaccional.

### Negativas

- Incremento en la cantidad de tecnologías a administrar.
- Redis requiere políticas de expiración e invalidación.
- Dependencia externa para almacenamiento multimedia.

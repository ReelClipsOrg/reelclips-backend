package org.arquitectura.reelclipsv2.usuarios.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.usuarios.api.UsuariosFacade;
import org.arquitectura.reelclipsv2.usuarios.api.dto.PerfilInfo;
import org.arquitectura.reelclipsv2.usuarios.api.dto.UsuarioInfo;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Registro, autenticación y gestión de perfiles")
public class UsuarioController {

    private final UsuariosFacade facade;

    @Operation(
            summary = "Registrar usuario",
            description = "RF-01 / RN-01 / RN-03 - Crea una cuenta nueva con username único, email y contraseña. " +
                    "Genera automáticamente un canal personal asociado al usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Email o username ya en uso")
            }
    )
    @PostMapping("/registro")
    public ResponseEntity<UsuarioInfo> registrar(
            @Parameter(description = "Nombre de usuario único", required = true, example = "usuarioA")
            @RequestParam String username,
            @Parameter(description = "Correo electrónico", required = true, example = "usuario@test.com")
            @RequestParam String email,
            @Parameter(description = "Contraseña", required = true)
            @RequestParam String password) {
        return ResponseEntity.ok(facade.registrar(username, email, password));
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "RF-02 - Autentica al usuario con email y contraseña. " +
                    "Retorna los datos del usuario si las credenciales son correctas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sesión iniciada exitosamente"),
                    @ApiResponse(responseCode = "403", description = "Credenciales incorrectas o cuenta desactivada"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<UsuarioInfo> login(
            @Parameter(description = "Correo electrónico", required = true, example = "usuario@test.com")
            @RequestParam String email,
            @Parameter(description = "Contraseña", required = true)
            @RequestParam String password) {
        return ResponseEntity.ok(facade.iniciarSesion(email, password));
    }

    @Operation(
            summary = "Ver perfil",
            description = "RF-05 - Retorna el perfil público de cualquier usuario: " +
                    "username, nombre de visualización, foto y descripción.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @GetMapping("/{id}/perfil")
    public ResponseEntity<PerfilInfo> verPerfil(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(facade.verPerfil(id));
    }

    @Operation(
            summary = "Listar perfiles públicos",
            description = "Retorna todos los perfiles públicos de usuarios con cuenta activa, excluyendo al usuario solicitante."
    )
    @GetMapping("/perfiles-publicos")
    public ResponseEntity<List<PerfilInfo>> listarPerfilesPublicos(
            @Parameter(description = "ID del usuario que consulta", required = true)
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(facade.listarPerfilesPublicos(usuarioId));
    }

    @Operation(
            summary = "Editar perfil",
            description = "RF-04 - Actualiza nombre de visualización, URL de foto y descripción. " +
                    "Para subir una nueva foto de perfil usa el endpoint POST /{id}/foto.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @PutMapping("/{id}/perfil")
    public ResponseEntity<UsuarioInfo> editarPerfil(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nombre de visualización", example = "Mi Nombre")
            @RequestParam String nombre,
            @Parameter(description = "URL de la foto de perfil")
            @RequestParam String foto,
            @Parameter(description = "Descripción del canal", example = "Bienvenidos a mi canal")
            @RequestParam String descripcion) {
        return ResponseEntity.ok(facade.editarPerfil(id, nombre, foto, descripcion));
    }

    @Operation(
            summary = "Subir foto de perfil",
            description = "RF-04 - Sube una imagen a Supabase Storage y actualiza la URL de la foto de perfil. " +
                    "Si ya existe una foto previa, la elimina de Supabase antes de subir la nueva.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = SubirFotoRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Foto subida y perfil actualizado"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error al subir la imagen a Supabase")
            }
    )
    @PostMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioInfo> subirFoto(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @Parameter(description = "Archivo de imagen (jpg, png, webp)", required = true)
            @RequestPart MultipartFile foto) {
        return ResponseEntity.ok(facade.subirFotoPerfil(id, foto));
    }

    @Operation(
            summary = "Cambiar username",
            description = "RF-04 / RN-04 - Cambia el nombre de usuario. " +
                    "Solo está permitido una vez cada 30 días para preservar la trazabilidad de la identidad.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Username actualizado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Han pasado menos de 30 días desde el último cambio, o el username ya está en uso"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @PatchMapping("/{id}/username")
    public ResponseEntity<UsuarioInfo> cambiarUsername(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevo username único", required = true, example = "mi_nuevo_username")
            @RequestParam String nuevoUsername) {
        return ResponseEntity.ok(facade.cambiarUsername(id, nuevoUsername));
    }

    @Operation(
            summary = "Desactivar cuenta",
            description = "RF-06 / RN-05 - Desactiva la cuenta del usuario. " +
                    "Los reels y mensajes se conservan durante 30 días antes de eliminarse permanentemente, " +
                    "permitiendo la recuperación de la cuenta en ese intervalo.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cuenta desactivada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del usuario a desactivar", required = true)
            @PathVariable Long id) {
        facade.desactivarCuenta(id);
        return ResponseEntity.noContent().build();
    }

    @Schema(name = "SubirFotoRequest", description = "Datos requeridos para subir foto de perfil")
    static class SubirFotoRequest {
        @Schema(description = "Archivo de imagen (jpg, png, webp)", type = "string", format = "binary")
        public MultipartFile foto;
    }
}

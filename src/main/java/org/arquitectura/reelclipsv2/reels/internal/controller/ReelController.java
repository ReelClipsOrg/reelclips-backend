package org.arquitectura.reelclipsv2.reels.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import org.arquitectura.reelclipsv2.reels.internal.proxy.ProxyReel;
import org.arquitectura.reelclipsv2.reels.internal.proxy.ServicioAlmacenamientoVideo;
import org.arquitectura.reelclipsv2.reels.internal.proxy.VideoStream;
import org.arquitectura.reelclipsv2.reels.internal.service.ReelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
@Tag(name = "Reels", description = "Publicación, edición, eliminación y visualización de reels")
public class ReelController {

    private final ReelService service;
    private final ProxyReel proxyReel;
    private final ServicioAlmacenamientoVideo servicioAlmacenamientoVideo;

    @Operation(
            summary = "Publicar reel",
            description = "RF-07 / RN-06 / RN-07 — Sube el video a Supabase Storage y registra el reel. " +
                    "Duración máxima 90 segundos, tamaño máximo 500 MB, mínimo una categoría obligatoria.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = PublicarReelRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reel publicado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Duración o tamaño inválido, o sin categoría asignada"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autenticado o sin permisos"),
                    @ApiResponse(responseCode = "500", description = "Error al subir el archivo a Supabase")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReelInfo> publicar(
            @Parameter(description = "ID del usuario que publica el reel", required = true)
            @RequestParam Long usuarioId,
            @Parameter(description = "Archivo de video (mp4, máx. 500MB)", required = true)
            @RequestPart MultipartFile video,
            @Parameter(description = "Descripción del reel")
            @RequestParam String descripcion,
            @Parameter(description = "Duración del video en segundos (máx. 90)", required = true)
            @RequestParam int duracionSegundos,
            @Parameter(description = "Tamaño del archivo en MB (máx. 500)", required = true)
            @RequestParam double tamanoMB,
            @Parameter(description = "IDs de las categorías asignadas (mínimo 1)", required = true)
            @RequestParam List<Long> categoriaIds) {

        String urlVideo = servicioAlmacenamientoVideo.guardar(video);
        return ResponseEntity.ok(
                service.publicar(usuarioId, urlVideo, descripcion,
                        duracionSegundos, tamanoMB, categoriaIds));
    }

    @Operation(
            summary = "Editar reel",
            description = "RF-08 / RN-08 — Actualiza descripción y categorías de un reel. " +
                    "Solo el propietario del reel puede realizar esta operación.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reel editado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "403", description = "El usuario no es propietario del reel"),
                    @ApiResponse(responseCode = "404", description = "Reel no encontrado")
            }
    )
    @PutMapping("/{reelId}")
    public ResponseEntity<ReelInfo> editar(
            @Parameter(description = "ID del reel a editar", required = true)
            @PathVariable Long reelId,
            @Parameter(description = "ID del usuario propietario", required = true)
            @RequestParam Long usuarioId,
            @Parameter(description = "Nueva descripción del reel")
            @RequestParam String descripcion,
            @Parameter(description = "Nuevos IDs de categorías (mínimo 1)", required = true)
            @RequestParam List<Long> categoriaIds) {
        return ResponseEntity.ok(service.editar(reelId, usuarioId, descripcion, categoriaIds));
    }

    @Operation(
            summary = "Eliminar reel",
            description = "RF-09 / RN-08 — Marca el reel como ELIMINADO. " +
                    "Solo el propietario puede eliminar su propio reel.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Reel eliminado exitosamente"),
                    @ApiResponse(responseCode = "403", description = "El usuario no es propietario del reel"),
                    @ApiResponse(responseCode = "404", description = "Reel no encontrado")
            }
    )
    @DeleteMapping("/{reelId}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del reel a eliminar", required = true)
            @PathVariable Long reelId,
            @Parameter(description = "ID del usuario propietario", required = true)
            @RequestParam Long usuarioId) {
        service.eliminar(reelId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar reel por ID",
            description = "RF-10 — Retorna los datos completos de un reel específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reel encontrado"),
                    @ApiResponse(responseCode = "404", description = "Reel no encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReelInfo> buscar(
            @Parameter(description = "ID del reel", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Listar reels públicos",
            description = "RF-10 / RN-09 — Retorna todos los reels con estado ACTIVO. " +
                    "Todos los reels son públicos por defecto.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de reels activos")
            }
    )
    @GetMapping
    public ResponseEntity<List<ReelInfo>> listarPublicos() {
        return ResponseEntity.ok(service.listarPublicos());
    }

    @Operation(
            summary = "Stream de video",
            description = "RF-10 — Patrón Proxy: verifica permisos con ServicioAutorizacion, " +
                    "consulta CacheVideo y si no está en caché obtiene el stream de ServicioAlmacenamientoVideo. " +
                    "Retorna la URL pública del video con metadatos del stream.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stream disponible"),
                    @ApiResponse(responseCode = "403", description = "Sin permiso para ver este reel"),
                    @ApiResponse(responseCode = "404", description = "Reel no encontrado")
            }
    )
    @GetMapping("/{reelId}/stream")
    public ResponseEntity<VideoStream> stream(
            @Parameter(description = "ID del reel a reproducir", required = true)
            @PathVariable Long reelId,
            @Parameter(description = "ID del usuario que solicita el stream", required = true)
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(proxyReel.obtenerStream(reelId, usuarioId));
    }

    @Operation(
            summary = "Reels por canal",
            description = "RF-10 — Lista todos los reels publicados en un canal específico, " +
                    "incluyendo todos los estados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de reels del canal"),
                    @ApiResponse(responseCode = "404", description = "Canal no encontrado")
            }
    )
    @GetMapping("/canal/{canalId}")
    public ResponseEntity<List<ReelInfo>> porCanal(
            @Parameter(description = "ID del canal", required = true)
            @PathVariable Long canalId) {
        return ResponseEntity.ok(service.listarPorCanal(canalId));
    }

    // Schema auxiliar para documentar el multipart en Swagger
    @Schema(name = "PublicarReelRequest", description = "Datos requeridos para publicar un reel")
    static class PublicarReelRequest {
        @Schema(description = "ID del usuario que publica", example = "1")
        public Long usuarioId;
        @Schema(description = "Archivo de video mp4 (máx. 500MB)", type = "string", format = "binary")
        public MultipartFile video;
        @Schema(description = "Descripción del reel", example = "Mi primer reel")
        public String descripcion;
        @Schema(description = "Duración en segundos (máx. 90)", example = "30")
        public int duracionSegundos;
        @Schema(description = "Tamaño en MB (máx. 500)", example = "10.5")
        public double tamanoMB;
        @Schema(description = "IDs de categorías (mínimo 1)", example = "[1, 2]")
        public List<Long> categoriaIds;
    }
}

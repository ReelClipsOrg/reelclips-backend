package org.arquitectura.reelclipsv2.reels.api.dto;

import org.arquitectura.reelclipsv2.shared.enums.EstadoReel;
import java.time.LocalDateTime;
import java.util.List;

public record ReelInfo(
        Long id,
        String urlVideo,
        String urlMiniatura,
        String descripcion,
        int duracionSegundos,
        double tamanoArchivoMB,
        EstadoReel estado,
        LocalDateTime fechaPublicacion,
        int contadorLikes,
        int contadorComentarios,
        Long canalId,
        List<String> categorias
) {}

package org.arquitectura.reelclipsv2.interacciones.api.dto;

import java.time.LocalDateTime;

public record InteraccionInfo(
        Long id,
        String tipo,
        Long usuarioId,
        Long reelId,
        LocalDateTime fecha
) {}

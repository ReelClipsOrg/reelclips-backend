package org.arquitectura.reelclipsv2.chat.api.dto;

import org.arquitectura.reelclipsv2.shared.enums.TipoMensaje;
import java.time.LocalDateTime;

public record MensajeInfo(
        Long id,
        Long conversacionId,
        Long remitenteId,
        String contenido,
        TipoMensaje tipoContenido,
        Long reelReferidoId,
        LocalDateTime fechaEnvio
) {}

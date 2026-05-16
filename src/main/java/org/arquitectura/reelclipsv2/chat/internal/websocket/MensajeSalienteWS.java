package org.arquitectura.reelclipsv2.chat.internal.websocket;

import org.arquitectura.reelclipsv2.shared.enums.TipoMensaje;
import java.time.LocalDateTime;

public record MensajeSalienteWS(
        Long id,
        Long conversacionId,
        Long remitenteId,
        String contenido,
        TipoMensaje tipoContenido,
        Long reelReferidoId,
        LocalDateTime fechaEnvio
) {}

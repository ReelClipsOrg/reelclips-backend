package org.arquitectura.reelclipsv2.chat.internal.websocket;

import org.arquitectura.reelclipsv2.shared.enums.TipoMensaje;

public record MensajeEntranteWS(
        Long conversacionId,
        Long remitenteId,
        String contenido,
        TipoMensaje tipoContenido,
        Long reelReferidoId
) {}

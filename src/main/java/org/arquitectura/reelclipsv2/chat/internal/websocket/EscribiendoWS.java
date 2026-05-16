package org.arquitectura.reelclipsv2.chat.internal.websocket;

public record EscribiendoWS(
        Long remitenteId,
        Long destinatarioId,
        Long conversacionId,
        boolean escribiendo
) {}

package org.arquitectura.reelclipsv2.interacciones.internal.observer;

import lombok.extern.slf4j.Slf4j;
import org.arquitectura.reelclipsv2.interacciones.internal.model.EventoInteraccion;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificadorAutor implements IObservador {

    @Override
    public void actualizar(EventoInteraccion evento) {
        // En producción: enviar notificación push o email al autor
        log.info("[NotificadorAutor] Usuario {} hizo {} en reel {}",
                evento.getUsuarioId(), evento.getTipoEvento(), evento.getReelId());
    }
}
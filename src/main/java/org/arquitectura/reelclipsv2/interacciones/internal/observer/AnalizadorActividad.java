package org.arquitectura.reelclipsv2.interacciones.internal.observer;

import lombok.extern.slf4j.Slf4j;
import org.arquitectura.reelclipsv2.interacciones.internal.model.EventoInteraccion;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnalizadorActividad implements IObservador {

    @Override
    public void actualizar(EventoInteraccion evento) {
        // En producción: registrar en sistema de analítica
        log.info("[AnalizadorActividad] Evento registrado: tipo={} usuario={} reel={}",
                evento.getTipoEvento(), evento.getUsuarioId(), evento.getReelId());
    }
}

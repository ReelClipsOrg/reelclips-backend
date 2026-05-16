package org.arquitectura.reelclipsv2.interacciones.internal.observer;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.interacciones.internal.model.EventoInteraccion;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PublicadorEventosInteraccion {

    private final List<IObservador> observadores;

    public void publicar(EventoInteraccion evento) {
        observadores.forEach(o -> o.actualizar(evento));
    }
}

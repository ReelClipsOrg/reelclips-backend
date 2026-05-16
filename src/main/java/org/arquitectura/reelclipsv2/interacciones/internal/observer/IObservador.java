package org.arquitectura.reelclipsv2.interacciones.internal.observer;

import org.arquitectura.reelclipsv2.interacciones.internal.model.EventoInteraccion;

public interface IObservador {
    void actualizar(EventoInteraccion evento);
}

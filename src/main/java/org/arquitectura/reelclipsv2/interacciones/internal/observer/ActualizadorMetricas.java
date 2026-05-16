package org.arquitectura.reelclipsv2.interacciones.internal.observer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arquitectura.reelclipsv2.interacciones.internal.model.EventoInteraccion;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.reels.internal.repository.IReelRepository;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActualizadorMetricas implements IObservador {

    private final IReelRepository reelRepo;

    @Override
    public void actualizar(EventoInteraccion evento) {
        Reel reel = reelRepo.findById(evento.getReelId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Reel no encontrado"));

        switch (evento.getTipoEvento()) {
            case "LIKE"       -> reel.setContadorLikes(reel.getContadorLikes() + 1);
            case "UNLIKE"     -> reel.setContadorLikes(Math.max(0, reel.getContadorLikes() - 1));
            case "COMENTARIO" -> reel.setContadorComentarios(reel.getContadorComentarios() + 1);
            case "DEL_COMENTARIO" -> reel.setContadorComentarios(Math.max(0, reel.getContadorComentarios() - 1));
        }
        reelRepo.save(reel);
        log.info("[ActualizadorMetricas] Métricas actualizadas para reel {}", evento.getReelId());
    }
}

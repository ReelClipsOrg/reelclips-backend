package org.arquitectura.reelclipsv2.feed.internal.service;


import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.shared.enums.EstadoReel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioFiltroVisibilidad {

    // RN-10: solo reels activos
    // RN-12: excluir reels propios del usuario
    public List<Reel> filtrar(List<Reel> reels, Long usuarioId) {
        return reels.stream()
                .filter(r -> r.getEstado() == EstadoReel.ACTIVO)
                .filter(r -> !r.getCanal().getUsuario().getId().equals(usuarioId))
                .toList();
    }
}

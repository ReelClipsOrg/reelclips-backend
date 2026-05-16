package org.arquitectura.reelclipsv2.interacciones.internal.repository;

import org.arquitectura.reelclipsv2.interacciones.internal.model.Reaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IReaccionRepository extends JpaRepository<Reaccion, Long> {
    Optional<Reaccion> findByUsuarioIdAndReelId(Long usuarioId, Long reelId);
    boolean existsByUsuarioIdAndReelId(Long usuarioId, Long reelId);
    long countByReelId(Long reelId);
}

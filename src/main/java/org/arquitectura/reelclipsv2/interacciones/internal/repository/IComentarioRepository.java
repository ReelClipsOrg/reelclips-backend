package org.arquitectura.reelclipsv2.interacciones.internal.repository;

import org.arquitectura.reelclipsv2.interacciones.internal.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByReelId(Long reelId);
    List<Comentario> findByUsuarioId(Long usuarioId);
}

package org.arquitectura.reelclipsv2.reels.internal.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.shared.enums.EstadoReel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IReelRepository extends JpaRepository<Reel, Long> {
    @EntityGraph(attributePaths = {"categorias", "canal", "canal.usuario"})
    List<Reel> findByEstado(EstadoReel estado);
    List<Reel> findByCanalId(Long canalId);
    List<Reel> findByCanalIdAndEstado(Long canalId, EstadoReel estado);
    List<Reel> findByCanalUsuarioIdNot(Long usuarioId);
}

package org.arquitectura.reelclipsv2.usuarios.internal.repository;


import org.arquitectura.reelclipsv2.usuarios.internal.model.Canal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ICanalRepository extends JpaRepository<Canal, Long> {
    Optional<Canal> findByUsuarioId(Long usuarioId);
}

package org.arquitectura.reelclipsv2.usuarios.internal.repository;

import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

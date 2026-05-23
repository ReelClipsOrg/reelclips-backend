package org.arquitectura.reelclipsv2.usuarios.internal.repository;

import org.arquitectura.reelclipsv2.shared.enums.EstadoCuenta;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<Usuario> findByEstadoCuentaExcludeThatId(EstadoCuenta estadoCuenta, Long id); //busca todas las cuentas públicas excepto la del usuario cuyo id está haciendo la solicitud
}

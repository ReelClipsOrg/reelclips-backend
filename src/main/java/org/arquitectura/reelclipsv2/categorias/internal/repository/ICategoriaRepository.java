package org.arquitectura.reelclipsv2.categorias.internal.repository;

import org.arquitectura.reelclipsv2.categorias.internal.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}

package org.arquitectura.reelclipsv2.categorias.internal.service;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.categorias.api.dto.CategoriaInfo;
import org.arquitectura.reelclipsv2.categorias.internal.model.Categoria;
import org.arquitectura.reelclipsv2.categorias.internal.repository.ICategoriaRepository;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.arquitectura.reelclipsv2.shared.exception.ReglaNegocioException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioAdminCategorias {

    private final ICategoriaRepository repo;

    // RF-19 Administración de categorías
    public CategoriaInfo crear(String nombre, String descripcion) {
        if (repo.existsByNombre(nombre))
            throw new ReglaNegocioException("Ya existe una categoría con ese nombre: " + nombre);
        Categoria c = Categoria.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .build();
        return toInfo(repo.save(c));
    }

    public CategoriaInfo editar(Long id, String nombre, String descripcion) {
        Categoria c = buscar(id);
        if (!c.getNombre().equals(nombre) && repo.existsByNombre(nombre))
            throw new ReglaNegocioException("Ya existe una categoría con ese nombre: " + nombre);
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        return toInfo(repo.save(c));
    }

    public void eliminar(Long id) {
        if (!repo.existsById(id))
            throw new RecursoNoEncontradoException("Categoría no encontrada: " + id);
        repo.deleteById(id);
    }

    public List<CategoriaInfo> listarTodas() {
        return repo.findAll().stream().map(this::toInfo).toList();
    }

    public CategoriaInfo buscarPorId(Long id) {
        return toInfo(buscar(id));
    }

    public boolean existePorId(Long id) {
        return repo.existsById(id);
    }

    private Categoria buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada: " + id));
    }

    public CategoriaInfo toInfo(Categoria c) {
        return new CategoriaInfo(c.getId(), c.getNombre(), c.getDescripcion());
    }
}

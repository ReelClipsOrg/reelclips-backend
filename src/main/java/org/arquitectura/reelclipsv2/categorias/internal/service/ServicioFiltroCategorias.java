package org.arquitectura.reelclipsv2.categorias.internal.service;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.categorias.api.dto.CategoriaInfo;
import org.arquitectura.reelclipsv2.categorias.internal.model.Categoria;
import org.arquitectura.reelclipsv2.categorias.internal.repository.ICategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioFiltroCategorias {

    private final ICategoriaRepository repo;

    // RF-21 Filtrado por categorías
    public List<CategoriaInfo> filtrarPorNombres(List<String> nombres) {
        return repo.findAll().stream()
                .filter(c -> nombres.contains(c.getNombre()))
                .map(c -> new CategoriaInfo(c.getId(), c.getNombre(), c.getDescripcion()))
                .toList();
    }

    public List<Long> obtenerIds(List<String> nombres) {
        return repo.findAll().stream()
                .filter(c -> nombres.contains(c.getNombre()))
                .map(Categoria::getId)
                .toList();
    }
}

package org.arquitectura.reelclipsv2.categorias;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.categorias.api.ICategoriaModuloApi;
import org.arquitectura.reelclipsv2.categorias.api.dto.CategoriaInfo;
import org.arquitectura.reelclipsv2.categorias.internal.service.ServicioAdminCategorias;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoriasModulo implements ICategoriaModuloApi {

    private final ServicioAdminCategorias service;

    @Override
    public CategoriaInfo buscarPorId(Long id) {
        return service.buscarPorId(id);
    }

    @Override
    public List<CategoriaInfo> listarTodas() {
        return service.listarTodas();
    }

    @Override
    public boolean existePorId(Long id) {
        return service.existePorId(id);
    }
}

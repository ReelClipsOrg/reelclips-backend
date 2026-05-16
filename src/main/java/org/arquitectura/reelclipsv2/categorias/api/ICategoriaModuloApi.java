package org.arquitectura.reelclipsv2.categorias.api;

import org.arquitectura.reelclipsv2.categorias.api.dto.CategoriaInfo;
import java.util.List;

public interface ICategoriaModuloApi {
    CategoriaInfo buscarPorId(Long id);
    List<CategoriaInfo> listarTodas();
    boolean existePorId(Long id);
}

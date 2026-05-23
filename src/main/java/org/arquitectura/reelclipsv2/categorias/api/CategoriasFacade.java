package org.arquitectura.reelclipsv2.categorias.api;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.categorias.api.dto.CategoriaInfo;
import org.arquitectura.reelclipsv2.categorias.internal.service.ServicioAdminCategorias;
import org.arquitectura.reelclipsv2.categorias.internal.service.ServicioFiltroCategorias;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoriasFacade implements ICategoriaModuloApi {

    private final ServicioAdminCategorias adminService;
    private final ServicioFiltroCategorias filtroService;

    public CategoriaInfo crear(String nombre, String descripcion) {
        return adminService.crear(nombre, descripcion);
    }

    public CategoriaInfo editar(Long id, String nombre, String descripcion) {
        return adminService.editar(id, nombre, descripcion);
    }

    public void eliminar(Long id) {
        adminService.eliminar(id);
    }

    public List<CategoriaInfo> filtrarPorNombres(List<String> nombres) {
        return filtroService.filtrarPorNombres(nombres);
    }

    @Override
    public CategoriaInfo buscarPorId(Long id) {
        return adminService.buscarPorId(id);
    }

    @Override
    public List<CategoriaInfo> listarTodas() {
        return adminService.listarTodas();
    }

    @Override
    public boolean existePorId(Long id) {
        return adminService.existePorId(id);
    }
}

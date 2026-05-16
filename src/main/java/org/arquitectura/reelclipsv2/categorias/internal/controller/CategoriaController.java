package org.arquitectura.reelclipsv2.categorias.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.arquitectura.reelclipsv2.categorias.api.dto.CategoriaInfo;
import org.arquitectura.reelclipsv2.categorias.internal.service.ServicioAdminCategorias;
import org.arquitectura.reelclipsv2.categorias.internal.service.ServicioFiltroCategorias;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías")
public class CategoriaController {

    private final ServicioAdminCategorias adminService;
    private final ServicioFiltroCategorias filtroService;

    @Operation(summary = "Listar categorías", description = "RF-19 — Retorna todas las categorías disponibles")
    @GetMapping
    public ResponseEntity<List<CategoriaInfo>> listar() {
        return ResponseEntity.ok(adminService.listarTodas());
    }

    @Operation(summary = "Buscar categoría", description = "RF-19 — Retorna una categoría por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaInfo> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.buscarPorId(id));
    }

    @Operation(summary = "Crear categoría", description = "RF-19 — Solo administradores pueden crear categorías")
    @PostMapping
    public ResponseEntity<CategoriaInfo> crear(
            @RequestParam String nombre,
            @RequestParam String descripcion) {
        return ResponseEntity.ok(adminService.crear(nombre, descripcion));
    }

    @Operation(summary = "Editar categoría", description = "RF-19 — Solo administradores pueden editar categorías")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaInfo> editar(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String descripcion) {
        return ResponseEntity.ok(adminService.editar(id, nombre, descripcion));
    }

    @Operation(summary = "Eliminar categoría", description = "RF-19 — Solo administradores pueden eliminar categorías")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        adminService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Filtrar por nombres", description = "RF-21 — Retorna categorías que coincidan con los nombres indicados")
    @GetMapping("/filtrar")
    public ResponseEntity<List<CategoriaInfo>> filtrar(@RequestParam List<String> nombres) {
        return ResponseEntity.ok(filtroService.filtrarPorNombres(nombres));
    }
}

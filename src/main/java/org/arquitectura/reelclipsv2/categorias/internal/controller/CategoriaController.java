package org.arquitectura.reelclipsv2.categorias.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.arquitectura.reelclipsv2.categorias.api.CategoriasFacade;
import org.arquitectura.reelclipsv2.categorias.api.dto.CategoriaInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías")
public class CategoriaController {

    private final CategoriasFacade facade;

    @Operation(summary = "Listar categorías", description = "RF-19 — Retorna todas las categorías disponibles")
    @GetMapping
    public ResponseEntity<List<CategoriaInfo>> listar() {
        return ResponseEntity.ok(facade.listarTodas());
    }

    @Operation(summary = "Buscar categoría", description = "RF-19 — Retorna una categoría por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaInfo> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(facade.buscarPorId(id));
    }

    @Operation(summary = "Crear categoría", description = "RF-19 — Solo administradores pueden crear categorías")
    @PostMapping
    public ResponseEntity<CategoriaInfo> crear(
            @RequestParam String nombre,
            @RequestParam String descripcion) {
        return ResponseEntity.ok(facade.crear(nombre, descripcion));
    }

    @Operation(summary = "Editar categoría", description = "RF-19 — Solo administradores pueden editar categorías")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaInfo> editar(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String descripcion) {
        return ResponseEntity.ok(facade.editar(id, nombre, descripcion));
    }

    @Operation(summary = "Eliminar categoría", description = "RF-19 — Solo administradores pueden eliminar categorías")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facade.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Filtrar por nombres", description = "RF-21 — Retorna categorías que coincidan con los nombres indicados")
    @GetMapping("/filtrar")
    public ResponseEntity<List<CategoriaInfo>> filtrar(@RequestParam List<String> nombres) {
        return ResponseEntity.ok(facade.filtrarPorNombres(nombres));
    }
}

package org.arquitectura.reelclipsv2.usuarios.api.dto;

public record PerfilInfo(
        Long id,
        String username,
        String nombreVisualizacion,
        String fotoPerfil,
        String descripcion
) {}

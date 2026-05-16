package org.arquitectura.reelclipsv2.usuarios.api.dto;

import org.arquitectura.reelclipsv2.shared.enums.EstadoCuenta;
import java.time.LocalDateTime;

public record UsuarioInfo(
        Long id,
        String username,
        String email,
        String nombreVisualizacion,
        String fotoPerfil,
        String descripcion,
        EstadoCuenta estadoCuenta,
        LocalDateTime fechaRegistro
) {}
package org.arquitectura.reelclipsv2.usuarios.internal.model;


import jakarta.persistence.*;
import lombok.*;
import org.arquitectura.reelclipsv2.shared.enums.EstadoCuenta;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String nombreVisualizacion;
    private String fotoPerfil;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuenta estadoCuenta = EstadoCuenta.ACTIVA;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    private LocalDate ultimoCambioUsername;
    private LocalDateTime fechaDesactivacion;
}

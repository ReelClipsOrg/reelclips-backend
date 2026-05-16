package org.arquitectura.reelclipsv2.interacciones.internal.model;

import jakarta.persistence.*;
import lombok.*;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reacciones",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "reel_id"})
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "reel_id", nullable = false)
    private Reel reel;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}

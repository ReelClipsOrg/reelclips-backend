package org.arquitectura.reelclipsv2.interacciones.internal.model;

import jakarta.persistence.*;
import lombok.*;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comentario {

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
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}

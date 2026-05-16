package org.arquitectura.reelclipsv2.reels.internal.model;


import jakarta.persistence.*;
import lombok.*;
import org.arquitectura.reelclipsv2.categorias.internal.model.Categoria;
import org.arquitectura.reelclipsv2.shared.enums.EstadoReel;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Canal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reels")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String urlVideo;

    private String urlMiniatura;

    private String descripcion;

    @Column(nullable = false)
    private int duracionSegundos;

    @Column(nullable = false)
    private double tamanoArchivoMB;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReel estado = EstadoReel.ACTIVO;

    @Column(nullable = false)
    private LocalDateTime fechaPublicacion = LocalDateTime.now();

    private int contadorLikes = 0;
    private int contadorComentarios = 0;

    @ManyToOne
    @JoinColumn(name = "canal_id", nullable = false)
    private Canal canal;

    // RN-07: obligatorio mínimo una categoría
    @ManyToMany
    @JoinTable(
            name = "reel_categorias",
            joinColumns = @JoinColumn(name = "reel_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias;
}

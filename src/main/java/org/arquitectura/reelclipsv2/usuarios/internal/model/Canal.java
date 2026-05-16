package org.arquitectura.reelclipsv2.usuarios.internal.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "canales")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Canal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}

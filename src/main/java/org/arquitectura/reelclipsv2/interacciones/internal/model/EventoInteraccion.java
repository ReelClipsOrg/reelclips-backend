package org.arquitectura.reelclipsv2.interacciones.internal.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos_interaccion")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoInteraccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipoEvento; // LIKE, COMENTARIO

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Long reelId;

    @Column(nullable = false)
    private LocalDateTime fechaEvento = LocalDateTime.now();
}

package org.arquitectura.reelclipsv2.chat.internal.model;

import jakarta.persistence.*;
import lombok.*;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversaciones")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RN-16: exactamente dos participantes
    @ManyToOne
    @JoinColumn(name = "usuario1_id", nullable = false)
    private Usuario usuario1;

    @ManyToOne
    @JoinColumn(name = "usuario2_id", nullable = false)
    private Usuario usuario2;

    @Column(nullable = false)
    private LocalDateTime fechaInicio = LocalDateTime.now();
}

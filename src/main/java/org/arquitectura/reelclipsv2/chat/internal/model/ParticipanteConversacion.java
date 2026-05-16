package org.arquitectura.reelclipsv2.chat.internal.model;

import jakarta.persistence.*;
import lombok.*;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import java.time.LocalDateTime;

@Entity
@Table(name = "participantes_conversacion")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipanteConversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversacion_id", nullable = false)
    private Conversacion conversacion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // RN-19: eliminación por vista sin afectar al otro participante
    private boolean activo = true;
    private LocalDateTime fechaEliminacion;
}

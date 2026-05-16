package org.arquitectura.reelclipsv2.chat.internal.model;

import jakarta.persistence.*;
import lombok.*;
import org.arquitectura.reelclipsv2.shared.enums.TipoMensaje;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversacion_id", nullable = false)
    private Conversacion conversacion;

    @ManyToOne
    @JoinColumn(name = "remitente_id", nullable = false)
    private Usuario remitente;

    @Column(nullable = false)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMensaje tipoContenido = TipoMensaje.TEXTO;

    // RN-19: referencia opcional a reel
    private Long reelReferidoId;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio = LocalDateTime.now();
}

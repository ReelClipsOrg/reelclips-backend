package org.arquitectura.reelclipsv2.reels.internal.proxy;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoStream {
    private Long idStream;
    private String url;
    private String formato;
    private String resolucion;
    private LocalDateTime fechaExpiracion;

    public boolean esValido() {
        return fechaExpiracion != null &&
                fechaExpiracion.isAfter(LocalDateTime.now());
    }
}

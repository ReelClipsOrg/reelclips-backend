package org.arquitectura.reelclipsv2.reels.internal.proxy;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.shared.enums.EstadoReel;
import org.arquitectura.reelclipsv2.usuarios.api.IUsuarioModuloApi;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioAutorizacion {

    private final IUsuarioModuloApi usuarioModuloApi;

    // RN-02: verifica que el usuario pueda ver el reel
    public boolean puedeVerReel(Long usuarioId, Reel reel) {
        if (reel.getEstado() != EstadoReel.ACTIVO) return false;
        return true;
    }

    // RN-02: verifica que el usuario pueda interactuar
    public boolean puedeInteractuar(Long usuarioId) {
        return usuarioId != null && usuarioModuloApi.estaActivo(usuarioId);
    }
}

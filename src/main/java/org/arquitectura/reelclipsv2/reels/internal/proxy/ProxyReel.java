package org.arquitectura.reelclipsv2.reels.internal.proxy;


import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.reels.internal.repository.IReelRepository;
import org.arquitectura.reelclipsv2.shared.exception.AccesoDenegadoException;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProxyReel {

    private final ServicioAutorizacion autorizacion;
    private final CacheVideo cache;
    private final ServicioAlmacenamientoVideo almacenamiento;
    private final IReelRepository reelRepo;

    public VideoStream obtenerStream(Long idReel, Long usuarioId) {
        Reel reel = reelRepo.findById(idReel)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reel no encontrado: " + idReel));

        // 1. Verificar autorización
        if (!autorizacion.puedeVerReel(usuarioId, reel))
            throw new AccesoDenegadoException("No tienes permiso para ver este reel");

        // 2. Buscar en caché
        VideoStream stream = cache.buscar(idReel);
        if (stream != null) return stream;

        // 3. Obtener del almacenamiento y cachear
        stream = almacenamiento.obtenerStream(idReel);
        cache.guardar(idReel, stream);
        return stream;
    }
}

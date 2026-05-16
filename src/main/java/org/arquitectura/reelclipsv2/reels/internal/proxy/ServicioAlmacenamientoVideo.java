package org.arquitectura.reelclipsv2.reels.internal.proxy;

import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.reels.internal.repository.IReelRepository;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.arquitectura.reelclipsv2.shared.storage.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ServicioAlmacenamientoVideo {

    private final IReelRepository reelRepo;
    private final SupabaseStorageService storageService;

    public VideoStream obtenerStream(Long idReel) {
        Reel reel = reelRepo.findById(idReel)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reel no encontrado: " + idReel));
        return VideoStream.builder()
                .idStream(idReel)
                .url(reel.getUrlVideo())
                .formato("mp4")
                .resolucion("1080p")
                .fechaExpiracion(LocalDateTime.now().plusHours(1))
                .build();
    }

    // Sube el video a Supabase y retorna la URL pública
    public String guardar(MultipartFile archivo) {
        return storageService.subirVideo(archivo);
    }

    public void eliminar(String urlVideo) {
        storageService.eliminar(urlVideo, "reels");
    }
}

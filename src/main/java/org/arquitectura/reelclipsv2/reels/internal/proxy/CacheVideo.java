package org.arquitectura.reelclipsv2.reels.internal.proxy;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheVideo {

    private final Map<Long, VideoStream> cache = new ConcurrentHashMap<>();

    public VideoStream buscar(Long idReel) {
        VideoStream stream = cache.get(idReel);
        if (stream != null && stream.esValido()) return stream;
        cache.remove(idReel);
        return null;
    }

    public void guardar(Long idReel, VideoStream stream) {
        cache.put(idReel, stream);
    }

    public void invalidar(Long idReel) {
        cache.remove(idReel);
    }
}

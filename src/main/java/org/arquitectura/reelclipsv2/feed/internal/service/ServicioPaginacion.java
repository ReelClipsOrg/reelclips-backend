package org.arquitectura.reelclipsv2.feed.internal.service;

import org.arquitectura.reelclipsv2.feed.api.dto.FeedResponse;
import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.reels.internal.service.ReelService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioPaginacion {

    private static final int TAMANO_PAGINA = 10;

    // RF-22, RN-14: scroll infinito paginado
    public FeedResponse paginar(List<Reel> reels, int pagina, ReelService reelService) {
        int total = reels.size();
        int totalPaginas = (int) Math.ceil((double) total / TAMANO_PAGINA);
        int desde = pagina * TAMANO_PAGINA;
        int hasta = Math.min(desde + TAMANO_PAGINA, total);

        List<ReelInfo> resultado = (desde >= total)
                ? List.of()
                : reels.subList(desde, hasta).stream()
                  .map(reelService::toInfo)
                  .toList();

        return new FeedResponse(
                resultado,
                pagina,
                totalPaginas,
                total,
                pagina < totalPaginas - 1
        );
    }
}

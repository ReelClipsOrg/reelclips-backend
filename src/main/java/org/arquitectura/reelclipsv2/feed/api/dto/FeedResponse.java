package org.arquitectura.reelclipsv2.feed.api.dto;

import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import java.util.List;

public record FeedResponse(
        List<ReelInfo> reels,
        int paginaActual,
        int totalPaginas,
        long totalElementos,
        boolean hayMas
) {}

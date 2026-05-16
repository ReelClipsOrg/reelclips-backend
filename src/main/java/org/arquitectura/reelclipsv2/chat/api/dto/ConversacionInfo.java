package org.arquitectura.reelclipsv2.chat.api.dto;

import java.time.LocalDateTime;

public record ConversacionInfo(
        Long id,
        Long usuario1Id,
        Long usuario2Id,
        LocalDateTime fechaInicio
) {}

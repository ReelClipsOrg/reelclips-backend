package org.arquitectura.reelclipsv2.chat.internal.repository;

import org.arquitectura.reelclipsv2.chat.internal.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IMensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByConversacionIdOrderByFechaEnvioAsc(Long conversacionId);
}

package org.arquitectura.reelclipsv2.interacciones.internal.service;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.interacciones.api.dto.InteraccionInfo;
import org.arquitectura.reelclipsv2.interacciones.internal.model.Comentario;
import org.arquitectura.reelclipsv2.interacciones.internal.model.EventoInteraccion;
import org.arquitectura.reelclipsv2.interacciones.internal.model.Reaccion;
import org.arquitectura.reelclipsv2.interacciones.internal.observer.PublicadorEventosInteraccion;
import org.arquitectura.reelclipsv2.interacciones.internal.repository.IComentarioRepository;
import org.arquitectura.reelclipsv2.interacciones.internal.repository.IReaccionRepository;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.reels.internal.repository.IReelRepository;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.arquitectura.reelclipsv2.shared.exception.ReglaNegocioException;
import org.arquitectura.reelclipsv2.shared.exception.AccesoDenegadoException;
import org.arquitectura.reelclipsv2.usuarios.api.IUsuarioModuloApi;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import org.arquitectura.reelclipsv2.usuarios.internal.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InteraccionService {

    private final IReaccionRepository reaccionRepo;
    private final IComentarioRepository comentarioRepo;
    private final IReelRepository reelRepo;
    private final IUsuarioRepository usuarioRepo;
    private final IUsuarioModuloApi usuarioModuloApi;
    private final PublicadorEventosInteraccion publicador;

    // RF-12 Reaccionar
    public InteraccionInfo darLike(Long usuarioId, Long reelId) {
        validarUsuarioActivo(usuarioId);
        if (reaccionRepo.existsByUsuarioIdAndReelId(usuarioId, reelId))
            throw new ReglaNegocioException("Ya reaccionaste a este reel");

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        Reel reel = reelRepo.findById(reelId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reel no encontrado"));

        Reaccion reaccion = Reaccion.builder()
                .usuario(usuario)
                .reel(reel)
                .fechaCreacion(LocalDateTime.now())
                .build();
        reaccionRepo.save(reaccion);

        publicador.publicar(EventoInteraccion.builder()
                .tipoEvento("LIKE")
                .usuarioId(usuarioId)
                .reelId(reelId)
                .fechaEvento(LocalDateTime.now())
                .build());

        return new InteraccionInfo(reaccion.getId(), "LIKE", usuarioId, reelId, reaccion.getFechaCreacion());
    }

    // RF-13 Eliminar reacción
    public void quitarLike(Long usuarioId, Long reelId) {
        validarUsuarioActivo(usuarioId);
        Reaccion reaccion = reaccionRepo.findByUsuarioIdAndReelId(usuarioId, reelId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No has reaccionado a este reel"));
        reaccionRepo.delete(reaccion);

        publicador.publicar(EventoInteraccion.builder()
                .tipoEvento("UNLIKE")
                .usuarioId(usuarioId)
                .reelId(reelId)
                .fechaEvento(LocalDateTime.now())
                .build());
    }

    // RF-14 Comentar
    public InteraccionInfo comentar(Long usuarioId, Long reelId, String contenido) {
        validarUsuarioActivo(usuarioId);
        if (contenido == null || contenido.isBlank())
            throw new ReglaNegocioException("El comentario no puede estar vacío");

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        Reel reel = reelRepo.findById(reelId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reel no encontrado"));

        Comentario comentario = Comentario.builder()
                .usuario(usuario)
                .reel(reel)
                .contenido(contenido)
                .fechaCreacion(LocalDateTime.now())
                .build();
        comentarioRepo.save(comentario);

        publicador.publicar(EventoInteraccion.builder()
                .tipoEvento("COMENTARIO")
                .usuarioId(usuarioId)
                .reelId(reelId)
                .fechaEvento(LocalDateTime.now())
                .build());

        return new InteraccionInfo(comentario.getId(), "COMENTARIO", usuarioId, reelId, comentario.getFechaCreacion());
    }

    // RF-15 Eliminar comentario
    public void eliminarComentario(Long comentarioId, Long usuarioId) {
        Comentario comentario = comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Comentario no encontrado"));
        if (!comentario.getUsuario().getId().equals(usuarioId))
            throw new AccesoDenegadoException("No puedes eliminar este comentario");
        comentarioRepo.delete(comentario);

        publicador.publicar(EventoInteraccion.builder()
                .tipoEvento("DEL_COMENTARIO")
                .usuarioId(usuarioId)
                .reelId(comentario.getReel().getId())
                .fechaEvento(LocalDateTime.now())
                .build());
    }

    public List<InteraccionInfo> listarComentarios(Long reelId) {
        return comentarioRepo.findByReelId(reelId).stream()
                .map(c -> new InteraccionInfo(c.getId(), "COMENTARIO",
                        c.getUsuario().getId(), c.getReel().getId(), c.getFechaCreacion()))
                .toList();
    }

    private void validarUsuarioActivo(Long usuarioId) {
        if (!usuarioModuloApi.estaActivo(usuarioId))
            throw new AccesoDenegadoException("Debes tener una cuenta activa para interactuar");
    }
}
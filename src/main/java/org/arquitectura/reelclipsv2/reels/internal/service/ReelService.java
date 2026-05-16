package org.arquitectura.reelclipsv2.reels.internal.service;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.categorias.api.ICategoriaModuloApi;
import org.arquitectura.reelclipsv2.categorias.internal.model.Categoria;
import org.arquitectura.reelclipsv2.categorias.internal.repository.ICategoriaRepository;
import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.reels.internal.proxy.CacheVideo;
import org.arquitectura.reelclipsv2.reels.internal.repository.IReelRepository;
import org.arquitectura.reelclipsv2.shared.enums.EstadoReel;
import org.arquitectura.reelclipsv2.shared.exception.AccesoDenegadoException;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.arquitectura.reelclipsv2.shared.exception.ReglaNegocioException;
import org.arquitectura.reelclipsv2.usuarios.api.IUsuarioModuloApi;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Canal;
import org.arquitectura.reelclipsv2.usuarios.internal.repository.ICanalRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReelService {

    private final IReelRepository reelRepo;
    private final ICanalRepository canalRepo;
    private final ICategoriaRepository categoriaRepo;
    private final ICategoriaModuloApi categoriaModuloApi;
    private final IUsuarioModuloApi usuarioModuloApi;
    private final CacheVideo cache;

    // RF-07 Publicación
    public ReelInfo publicar(Long usuarioId, String urlVideo, String descripcion,
                             int duracionSegundos, double tamanoMB, List<Long> categoriaIds) {
        // RN-06: validar duración y tamaño
        if (duracionSegundos > 90)
            throw new ReglaNegocioException("El reel no puede superar los 90 segundos");
        if (tamanoMB > 500)
            throw new ReglaNegocioException("El archivo no puede superar los 500 MB");
        // RN-07: categoría obligatoria
        if (categoriaIds == null || categoriaIds.isEmpty())
            throw new ReglaNegocioException("Debes asignar al menos una categoría al reel");

        Canal canal = canalRepo.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Canal no encontrado para el usuario: " + usuarioId));

        List<Categoria> categorias = categoriaRepo.findAllById(categoriaIds);
        if (categorias.isEmpty())
            throw new ReglaNegocioException("Las categorías indicadas no existen");

        Reel reel = Reel.builder()
                .urlVideo(urlVideo)
                .descripcion(descripcion)
                .duracionSegundos(duracionSegundos)
                .tamanoArchivoMB(tamanoMB)
                .estado(EstadoReel.ACTIVO)
                .fechaPublicacion(LocalDateTime.now())
                .canal(canal)
                .categorias(categorias)
                .build();

        return toInfo(reelRepo.save(reel));
    }

    // RF-08 Edición
    public ReelInfo editar(Long reelId, Long usuarioId, String descripcion, List<Long> categoriaIds) {
        Reel reel = buscar(reelId);
        validarPropietario(reel, usuarioId);

        if (categoriaIds == null || categoriaIds.isEmpty())
            throw new ReglaNegocioException("Debes asignar al menos una categoría");

        reel.setDescripcion(descripcion);
        reel.setCategorias(categoriaRepo.findAllById(categoriaIds));
        return toInfo(reelRepo.save(reel));
    }

    // RF-09 Eliminación
    public void eliminar(Long reelId, Long usuarioId) {
        Reel reel = buscar(reelId);
        validarPropietario(reel, usuarioId);
        reel.setEstado(EstadoReel.ELIMINADO);
        reelRepo.save(reel);
        cache.invalidar(reelId);
    }

    // RF-10 Visualización
    public ReelInfo buscarPorId(Long id) {
        return toInfo(buscar(id));
    }

    public boolean existePorId(Long id) {
        return reelRepo.existsById(id);
    }

    public List<ReelInfo> listarPublicos() {
        return reelRepo.findByEstado(EstadoReel.ACTIVO)
                .stream().map(this::toInfo).toList();
    }

    public List<ReelInfo> listarPorCanal(Long canalId) {
        return reelRepo.findByCanalId(canalId)
                .stream().map(this::toInfo).toList();
    }

    // RN-08: solo el propietario puede modificar
    private void validarPropietario(Reel reel, Long usuarioId) {
        if (!reel.getCanal().getUsuario().getId().equals(usuarioId))
            throw new AccesoDenegadoException("No tienes permiso para modificar este reel");
    }

    private Reel buscar(Long id) {
        return reelRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reel no encontrado: " + id));
    }

    public ReelInfo toInfo(Reel r) {
        return new ReelInfo(
                r.getId(), r.getUrlVideo(), r.getUrlMiniatura(),
                r.getDescripcion(), r.getDuracionSegundos(), r.getTamanoArchivoMB(),
                r.getEstado(), r.getFechaPublicacion(),
                r.getContadorLikes(), r.getContadorComentarios(),
                r.getCanal().getId(),
                r.getCategorias().stream().map(Categoria::getNombre).toList()
        );
    }
}

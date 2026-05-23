package org.arquitectura.reelclipsv2.usuarios.internal.service;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.shared.enums.EstadoCuenta;
import org.arquitectura.reelclipsv2.shared.exception.AccesoDenegadoException;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.arquitectura.reelclipsv2.shared.exception.ReglaNegocioException;
import org.arquitectura.reelclipsv2.shared.storage.SupabaseStorageService;
import org.arquitectura.reelclipsv2.usuarios.api.dto.PerfilInfo;
import org.arquitectura.reelclipsv2.usuarios.api.dto.UsuarioInfo;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Canal;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import org.arquitectura.reelclipsv2.usuarios.internal.repository.ICanalRepository;
import org.arquitectura.reelclipsv2.usuarios.internal.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final IUsuarioRepository usuarioRepo;
    private final ICanalRepository canalRepo;
    private final SupabaseStorageService storageService;

    public UsuarioInfo registrar(String username, String email, String password) {
        if (usuarioRepo.existsByEmail(email)) {
            throw new ReglaNegocioException("El email ya está registrado: " + email);
        }
        if (usuarioRepo.existsByUsername(username)) {
            throw new ReglaNegocioException("El username ya está en uso: " + username);
        }

        Usuario usuario = Usuario.builder()
                .username(username)
                .email(email)
                .passwordHash(password)
                .nombreVisualizacion(username)
                .estadoCuenta(EstadoCuenta.ACTIVA)
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuarioRepo.save(usuario);

        Canal canal = Canal.builder()
                .usuario(usuario)
                .fechaCreacion(LocalDateTime.now())
                .build();
        canalRepo.save(canal);

        return toInfo(usuario);
    }

    public UsuarioInfo iniciarSesion(String email, String password) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        if (!usuario.getPasswordHash().equals(password)) {
            throw new AccesoDenegadoException("Credenciales incorrectas");
        }
        if (usuario.getEstadoCuenta() == EstadoCuenta.DESACTIVADA) {
            throw new AccesoDenegadoException("La cuenta está desactivada");
        }
        return toInfo(usuario);
    }

    public UsuarioInfo editarPerfil(Long id, String nombre, String foto, String descripcion) {
        Usuario usuario = buscar(id);
        usuario.setNombreVisualizacion(nombre);
        usuario.setFotoPerfil(foto);
        usuario.setDescripcion(descripcion);
        return toInfo(usuarioRepo.save(usuario));
    }

    public UsuarioInfo subirFotoPerfil(Long id, MultipartFile archivo) {
        Usuario usuario = buscar(id);
        if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isBlank()) {
            storageService.eliminar(usuario.getFotoPerfil(), "imagenes-perfil");
        }
        String url = storageService.subirImagenPerfil(archivo);
        usuario.setFotoPerfil(url);
        return toInfo(usuarioRepo.save(usuario));
    }

    public UsuarioInfo cambiarUsername(Long id, String nuevoUsername) {
        Usuario usuario = buscar(id);
        if (usuario.getUltimoCambioUsername() != null &&
                usuario.getUltimoCambioUsername().plusDays(30).isAfter(LocalDate.now())) {
            throw new ReglaNegocioException("Solo puedes cambiar el username una vez cada 30 días");
        }
        if (usuarioRepo.existsByUsername(nuevoUsername)) {
            throw new ReglaNegocioException("El username ya está en uso: " + nuevoUsername);
        }
        usuario.setUsername(nuevoUsername);
        usuario.setUltimoCambioUsername(LocalDate.now());
        return toInfo(usuarioRepo.save(usuario));
    }

    public PerfilInfo verPerfil(Long id) {
        Usuario usuario = buscar(id);
        return toPerfilInfo(usuario);
    }

    public List<PerfilInfo> listarPerfilesPublicos(Long usuarioId) {
        if (!estaActivo(usuarioId)) {
            throw new AccesoDenegadoException("Debes tener una cuenta activa para consultar perfiles públicos");
        }

        return usuarioRepo.findByEstadoCuentaExcludeThatId(EstadoCuenta.ACTIVA, usuarioId)
                .stream()
                .map(this::toPerfilInfo)
                .toList();
    }

    public void desactivarCuenta(Long id) {
        Usuario usuario = buscar(id);
        usuario.setEstadoCuenta(EstadoCuenta.DESACTIVADA);
        usuario.setFechaDesactivacion(LocalDateTime.now());
        usuarioRepo.save(usuario);
    }

    public UsuarioInfo buscarPorId(Long id) {
        return toInfo(buscar(id));
    }

    public boolean existePorId(Long id) {
        return usuarioRepo.existsById(id);
    }

    public boolean estaActivo(Long id) {
        return usuarioRepo.findById(id)
                .map(usuario -> usuario.getEstadoCuenta() == EstadoCuenta.ACTIVA)
                .orElse(false);
    }

    private Usuario buscar(Long id) {
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }

    private PerfilInfo toPerfilInfo(Usuario usuario) {
        return new PerfilInfo(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombreVisualizacion(),
                usuario.getFotoPerfil(),
                usuario.getDescripcion()
        );
    }

    private UsuarioInfo toInfo(Usuario usuario) {
        return new UsuarioInfo(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombreVisualizacion(),
                usuario.getFotoPerfil(),
                usuario.getDescripcion(),
                usuario.getEstadoCuenta(),
                usuario.getFechaRegistro()
        );
    }
}

package org.arquitectura.reelclipsv2.usuarios.internal.service;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.shared.enums.EstadoCuenta;
import org.arquitectura.reelclipsv2.shared.exception.*;
import org.arquitectura.reelclipsv2.shared.storage.SupabaseStorageService;
import org.arquitectura.reelclipsv2.usuarios.api.dto.*;
import org.arquitectura.reelclipsv2.usuarios.internal.model.*;
import org.arquitectura.reelclipsv2.usuarios.internal.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final IUsuarioRepository usuarioRepo;
    private final ICanalRepository canalRepo;
    private final SupabaseStorageService storageService;

    // RF-01 Registro
    public UsuarioInfo registrar(String username, String email, String password) {
        if (usuarioRepo.existsByEmail(email))
            throw new ReglaNegocioException("El email ya está registrado: " + email);
        if (usuarioRepo.existsByUsername(username))
            throw new ReglaNegocioException("El username ya está en uso: " + username);

        Usuario u = Usuario.builder()
                .username(username)
                .email(email)
                .passwordHash(password) // en producción: BCrypt
                .nombreVisualizacion(username)
                .estadoCuenta(EstadoCuenta.ACTIVA)
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuarioRepo.save(u);

        // RN-03: canal personal único creado automáticamente
        Canal canal = Canal.builder()
                .usuario(u)
                .fechaCreacion(LocalDateTime.now())
                .build();
        canalRepo.save(canal);

        return toInfo(u);
    }

    // RF-02 Autenticación
    public UsuarioInfo iniciarSesion(String email, String password) {
        Usuario u = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        if (!u.getPasswordHash().equals(password))
            throw new AccesoDenegadoException("Credenciales incorrectas");
        if (u.getEstadoCuenta() == EstadoCuenta.DESACTIVADA)
            throw new AccesoDenegadoException("La cuenta está desactivada");
        return toInfo(u);
    }

    // RF-04 Edición de perfil
    public UsuarioInfo editarPerfil(Long id, String nombre, String foto, String descripcion) {
        Usuario u = buscar(id);
        u.setNombreVisualizacion(nombre);
        u.setFotoPerfil(foto);
        u.setDescripcion(descripcion);
        return toInfo(usuarioRepo.save(u));
    }

    // RF-04 Subir foto de perfil
    public UsuarioInfo subirFotoPerfil(Long id, MultipartFile archivo) {
        Usuario u = buscar(id);
        // Eliminar foto anterior si existe
        if (u.getFotoPerfil() != null && !u.getFotoPerfil().isBlank()) {
            storageService.eliminar(u.getFotoPerfil(), "imagenes-perfil");
        }
        String url = storageService.subirImagenPerfil(archivo);
        u.setFotoPerfil(url);
        return toInfo(usuarioRepo.save(u));
    }

    // RF-04 Cambio de username (RN-04: máximo una vez cada 30 días)
    public UsuarioInfo cambiarUsername(Long id, String nuevoUsername) {
        Usuario u = buscar(id);
        if (u.getUltimoCambioUsername() != null &&
                u.getUltimoCambioUsername().plusDays(30).isAfter(LocalDate.now()))
            throw new ReglaNegocioException("Solo puedes cambiar el username una vez cada 30 días");
        if (usuarioRepo.existsByUsername(nuevoUsername))
            throw new ReglaNegocioException("El username ya está en uso: " + nuevoUsername);
        u.setUsername(nuevoUsername);
        u.setUltimoCambioUsername(LocalDate.now());
        return toInfo(usuarioRepo.save(u));
    }

    // RF-05 Visualización de perfil
    public PerfilInfo verPerfil(Long id) {
        Usuario u = buscar(id);
        return new PerfilInfo(u.getId(), u.getUsername(),
                u.getNombreVisualizacion(), u.getFotoPerfil(), u.getDescripcion());
    }

    // RF-06 Desactivación
    public void desactivarCuenta(Long id) {
        Usuario u = buscar(id);
        u.setEstadoCuenta(EstadoCuenta.DESACTIVADA);
        u.setFechaDesactivacion(LocalDateTime.now());
        usuarioRepo.save(u);
    }

    public UsuarioInfo buscarPorId(Long id) {
        return toInfo(buscar(id));
    }

    public boolean existePorId(Long id) {
        return usuarioRepo.existsById(id);
    }

    public boolean estaActivo(Long id) {
        return usuarioRepo.findById(id)
                .map(u -> u.getEstadoCuenta() == EstadoCuenta.ACTIVA)
                .orElse(false);
    }

    private Usuario buscar(Long id) {
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }

    private UsuarioInfo toInfo(Usuario u) {
        return new UsuarioInfo(u.getId(), u.getUsername(), u.getEmail(),
                u.getNombreVisualizacion(), u.getFotoPerfil(), u.getDescripcion(),
                u.getEstadoCuenta(), u.getFechaRegistro());
    }
}

package org.arquitectura.reelclipsv2.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleRegla(ReglaNegocioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error(ex.getMessage(), 400));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error(ex.getMessage(), 404));
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Map<String, Object>> handleAcceso(AccesoDenegadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error(ex.getMessage(), 403));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("Error interno del servidor: " + ex.getMessage(), 500));
    }

    private Map<String, Object> error(String mensaje, int codigo) {
        return Map.of(
                "mensaje", mensaje,
                "codigo", codigo,
                "timestamp", LocalDateTime.now().toString()
        );
    }
}

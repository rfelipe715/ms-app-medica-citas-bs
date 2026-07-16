package cl.duoc.ms_citas_bs.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura cuando una cita no existe (404)
    @ExceptionHandler(CitaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarCitaNoEncontrada(CitaNotFoundException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, "Cita no encontrada", ex.getMessage());
    }

    // Captura cuando falla una validación de @Valid (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresDeValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            errores.put(campo, error.getDefaultMessage());
        });

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Datos inválidos");
        body.put("detalles", errores);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Propaga un 404 real desde ms-citas-db (p.ej. si se detectó tarde, tras un cambio de estado externo)
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontradoRemoto(FeignException.NotFound ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, "Recurso no encontrado", "El servicio remoto no encontró el recurso solicitado.");
    }

    // Cualquier otra falla de comunicación con ms-citas-db / ms-pacientes-bs (timeout, 500, conexión rechazada, etc.)
    @ExceptionHandler(ServicioNoDisponibleException.class)
    public ResponseEntity<Map<String, Object>> manejarServicioNoDisponible(ServicioNoDisponibleException ex) {
        return construirRespuesta(HttpStatus.BAD_GATEWAY, "Servicio no disponible", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(HttpStatus status, String error, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("mensaje", mensaje);
        return new ResponseEntity<>(body, status);
    }
}

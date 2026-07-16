package cl.duoc.ms_citas_bs.controller;

import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaUpdateDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaConPacienteDTO;
import cl.duoc.ms_citas_bs.service.CitasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
public class CitasController {

    @Autowired
    private CitasService citasService;

    @PostMapping
    public ResponseEntity<CitaDTO> guardarCita(@Valid @RequestBody CitaDTO citaDTO) {
        CitaDTO citaCreada = citasService.agendarCita(citaDTO);
        return new ResponseEntity<>(citaCreada, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CitaDTO>> obtenerCitas() {
        List<CitaDTO> citas = citasService.obtenerCitas();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> obtenerCitaPorId(@PathVariable Long id) {
        CitaDTO cita = citasService.obtenerCitaPorId(id);
        return ResponseEntity.ok(cita);
    }

    @GetMapping("/con-pacientes")
    public ResponseEntity<List<CitaConPacienteDTO>> obtenerCitasConPacientes() {
        List<CitaConPacienteDTO> citas = citasService.obtenerCitasConPacientes();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}/con-paciente")
    public ResponseEntity<CitaConPacienteDTO> obtenerCitaConPaciente(@PathVariable Long id) {
        CitaConPacienteDTO cita = citasService.obtenerCitaConPaciente(id);
        if (cita != null) {
            return ResponseEntity.ok(cita);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citasService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<CitaUpdateDTO> actualizarCita(@Valid @RequestBody CitaUpdateDTO cita) {
        CitaUpdateDTO citaActualizada = citasService.actualizarCita(cita);
        return ResponseEntity.ok(citaActualizada);
    }
}

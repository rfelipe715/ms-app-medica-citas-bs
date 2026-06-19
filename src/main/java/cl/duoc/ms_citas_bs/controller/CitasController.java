package cl.duoc.ms_citas_bs.controller;

import cl.duoc.ms_citas_bs.client.CitasDbRestClient;
import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaUpdateDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaConPacienteDTO;
import cl.duoc.ms_citas_bs.service.CitasService;
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
    public ResponseEntity<Void> guardarCita(@RequestBody CitaDTO citaDTO) {
        try {
            citasService.agendarCita(citaDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CitaDTO>> obtenerCitas() {
        try {
            List<CitaDTO> citas = citasService.obtenerCitas();
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/con-pacientes")
    public ResponseEntity<List<CitaConPacienteDTO>> obtenerCitasConPacientes() {
        try {
            List<CitaConPacienteDTO> citas = citasService.obtenerCitasConPacientes();
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/con-paciente")
    public ResponseEntity<CitaConPacienteDTO> obtenerCitaConPaciente(@PathVariable Long id) {
        try {
            CitaConPacienteDTO cita = citasService.obtenerCitaConPaciente(id);
            if (cita != null) {
                return ResponseEntity.ok(cita);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        try {
            citasService.eliminarCita(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<CitaUpdateDTO> actualizarCita(@RequestBody CitaUpdateDTO cita) {
        try {
            CitaUpdateDTO citaActualizada = citasService.actualizarCita(cita);
            return ResponseEntity.ok(citaActualizada);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package cl.duoc.ms_citas_bs.service;

import cl.duoc.ms_citas_bs.client.CitasDbRestClient;
import cl.duoc.ms_citas_bs.client.PacientesBsRestClient;
import cl.duoc.ms_citas_bs.exception.CitaNotFoundException;
import cl.duoc.ms_citas_bs.exception.ServicioNoDisponibleException;
import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaUpdateDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaConPacienteDTO;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitasService {

    private static final Logger log = LoggerFactory.getLogger(CitasService.class);

    @Autowired
    CitasDbRestClient citasDbRestClient;

    @Autowired
    private PacientesBsRestClient pacientesBsRestClient;

    public CitaDTO agendarCita(CitaDTO citaDTO) {
        try {
            CitaDTO guardada = citasDbRestClient.guardarCita(citaDTO);
            log.info("Cita agendada con id={}, pacienteId={}", guardada.getId(), guardada.getPacienteId());
            return guardada;
        } catch (FeignException e) {
            log.error("ms-citas-db no disponible al agendar cita para pacienteId={}: {}", citaDTO.getPacienteId(), e.getMessage());
            throw new ServicioNoDisponibleException("ms-citas-db", e);
        }
    }

    public List<CitaDTO> obtenerCitas() {
        return citasDbRestClient.obtenerCitas();
    }

    public CitaDTO obtenerCitaPorId(Long id) {
        try {
            return citasDbRestClient.obtenerCitaPorId(id);
        } catch (FeignException.NotFound e) {
            log.warn("Cita id={} no encontrada en ms-citas-db", id);
            throw new CitaNotFoundException(id);
        } catch (FeignException e) {
            log.error("ms-citas-db no disponible al buscar cita id={}: {}", id, e.getMessage());
            throw new ServicioNoDisponibleException("ms-citas-db", e);
        }
    }

    public List<CitaConPacienteDTO> obtenerCitasConPacientes() {
        return citasDbRestClient.obtenerCitas().stream()
            .map(this::enriquecerCita)
            .collect(Collectors.toList());
    }

    public void eliminarCita(Long id) {
        try {
            citasDbRestClient.eliminarCita(id);
            log.info("Cita id={} eliminada correctamente", id);
        } catch (FeignException.NotFound e) {
            log.warn("Intento de eliminar una cita inexistente, id={}", id);
            throw new CitaNotFoundException(id);
        } catch (FeignException e) {
            log.error("ms-citas-db no disponible al eliminar cita id={}: {}", id, e.getMessage());
            throw new ServicioNoDisponibleException("ms-citas-db", e);
        }
    }

    public CitaUpdateDTO actualizarCita(CitaUpdateDTO cita) {
        try {
            CitaUpdateDTO actualizada = citasDbRestClient.actualizarCita(cita);
            log.info("Cita id={} actualizada correctamente", cita.getId());
            return actualizada;
        } catch (FeignException.NotFound e) {
            log.warn("Intento de actualizar una cita inexistente, id={}", cita.getId());
            throw new CitaNotFoundException(cita.getId());
        } catch (FeignException e) {
            log.error("ms-citas-db no disponible al actualizar cita id={}: {}", cita.getId(), e.getMessage());
            throw new ServicioNoDisponibleException("ms-citas-db", e);
        }
    }

    public CitaConPacienteDTO obtenerCitaConPaciente(Long id) {
        CitaDTO cita = citasDbRestClient.obtenerCitas().stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);
        
        if (cita != null) {
            return enriquecerCita(cita);
        }
        return null;
    }

    private CitaConPacienteDTO enriquecerCita(CitaDTO citaDTO) {
        CitaConPacienteDTO conPaciente = new CitaConPacienteDTO();
        conPaciente.setId(citaDTO.getId());
        conPaciente.setPacienteId(citaDTO.getPacienteId());
        conPaciente.setFecha(citaDTO.getFecha());
        conPaciente.setHora(citaDTO.getHora());
        conPaciente.setEstado(citaDTO.getEstado());
        
        try {
            conPaciente.setPaciente(pacientesBsRestClient.obtenerPaciente(citaDTO.getPacienteId()));
        } catch (Exception e) {
            log.warn("No se pudo enriquecer la cita id={} con datos del paciente id={}: {}",
                    citaDTO.getId(), citaDTO.getPacienteId(), e.getMessage());
        }
        
        return conPaciente;
    }
}

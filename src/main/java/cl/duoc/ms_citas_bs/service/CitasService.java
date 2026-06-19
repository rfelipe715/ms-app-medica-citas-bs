package cl.duoc.ms_citas_bs.service;

import cl.duoc.ms_citas_bs.client.CitasDbRestClient;
import cl.duoc.ms_citas_bs.client.PacientesBsRestClient;
import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaUpdateDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaConPacienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitasService {

    @Autowired
    CitasDbRestClient citasDbRestClient;

    @Autowired
    private PacientesBsRestClient pacientesBsRestClient;

    public void agendarCita(CitaDTO citaDTO) {
        citasDbRestClient.guardarCita(citaDTO);
    }

    public List<CitaDTO> obtenerCitas() {
        return citasDbRestClient.obtenerCitas();
    }

    public List<CitaConPacienteDTO> obtenerCitasConPacientes() {
        return citasDbRestClient.obtenerCitas().stream()
            .map(this::enriquecerCita)
            .collect(Collectors.toList());
    }

    public void eliminarCita(Long id) {
        citasDbRestClient.eliminarCita(id);
    }

    public CitaUpdateDTO actualizarCita(CitaUpdateDTO cita) {
        return citasDbRestClient.actualizarCita(cita);
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
            // Silenciar error si no encuentra paciente
        }
        
        return conPaciente;
    }
}
    }
}

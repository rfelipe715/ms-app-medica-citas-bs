package cl.duoc.ms_citas_bs.service;

import cl.duoc.ms_citas_bs.client.CitasDbRestClient;
import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitasService {

    @Autowired
    CitasDbRestClient citasDbRestClient;

    public void agendarCita(CitaDTO citaDTO) {
        citasDbRestClient.guardarCita(citaDTO);
    }

    public List<CitaDTO> obtenerCitas() {
        return citasDbRestClient.obtenerCitas();
    }

    public void eliminarCita(Long id) {
        citasDbRestClient.eliminarCita(id);
    }

    public CitaUpdateDTO actualizarCita(CitaUpdateDTO cita) {
        return citasDbRestClient.actualizarCita(cita);
    }
}

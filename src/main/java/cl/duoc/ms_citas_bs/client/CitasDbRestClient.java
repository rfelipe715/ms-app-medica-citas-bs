package cl.duoc.ms_citas_bs.client;

import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "citas-db", url = "http://localhost:8092/api/v1/citas")
public interface CitasDbRestClient {

    @PostMapping
    public void guardarCita(@RequestBody CitaDTO citaDTO);

    @GetMapping
    public List<CitaDTO> obtenerCitas();

    @DeleteMapping
    public void eliminarCita(Long id);

    @PutMapping
    public CitaUpdateDTO actualizarCita(@RequestBody CitaUpdateDTO cita);
}

package cl.duoc.ms_citas_bs.client;

import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "citas-db", url = "${citas-db.url:http://localhost:8092/api/v1/citas}")
public interface CitasDbRestClient {

    @PostMapping
    CitaDTO guardarCita(@RequestBody CitaDTO citaDTO);

    @GetMapping
    List<CitaDTO> obtenerCitas();

    @GetMapping("/{id}")
    CitaDTO obtenerCitaPorId(@PathVariable("id") Long id);

    @DeleteMapping("/{id}")
    void eliminarCita(@PathVariable("id") Long id);

    @PutMapping
    CitaUpdateDTO actualizarCita(@RequestBody CitaUpdateDTO cita);
}

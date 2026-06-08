package cl.duoc.ms_citas_bs.client;

import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "citas-db", url = "http://localhost:8092/api/v1/citas")
public interface CitasDbRestClient {

    @PostMapping
    public void guardarCita(@RequestBody CitaDTO citaDTO);

    @GetMapping
    public List<CitaDTO> obtenerCitas();
}

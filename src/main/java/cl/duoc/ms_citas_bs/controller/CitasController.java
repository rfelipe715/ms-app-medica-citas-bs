package cl.duoc.ms_citas_bs.controller;

import cl.duoc.ms_citas_bs.client.CitasDbRestClient;
import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaUpdateDTO;
import cl.duoc.ms_citas_bs.service.CitasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
public class CitasController {

    @Autowired
    private CitasService citasService;

    @PostMapping()
    public void guardarCita(@RequestBody CitaDTO citaDTO) {
        citasService.agendarCita(citaDTO);
    }

    @GetMapping
    public List<CitaDTO> obtenerCitas() {
        return citasService.obtenerCitas();
    }

    @DeleteMapping("/{id}")
    public void eliminarCita(@RequestParam Long id) {
        citasService.eliminarCita(id);
    }

    @PutMapping
    public CitaUpdateDTO actualizarCita (@RequestBody CitaUpdateDTO cita) {
        return citasService.actualizarCita(cita);
    }
}

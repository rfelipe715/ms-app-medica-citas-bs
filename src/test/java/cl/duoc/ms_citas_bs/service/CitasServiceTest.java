package cl.duoc.ms_citas_bs.service;

import cl.duoc.ms_citas_bs.client.CitasDbRestClient;
import cl.duoc.ms_citas_bs.client.PacientesBsRestClient;
import cl.duoc.ms_citas_bs.model.dto.CitaConPacienteDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaDTO;
import cl.duoc.ms_citas_bs.model.dto.CitaUpdateDTO;
import cl.duoc.ms_citas_bs.model.dto.PacienteBffDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CitasServiceTest {

    @Mock
    private CitasDbRestClient citasDbRestClient;

    @Mock
    private PacientesBsRestClient pacientesBsRestClient;

    @InjectMocks
    private CitasService citasService;

    private CitaDTO citaDTO;

    @BeforeEach
    void setUp() {
        citaDTO = new CitaDTO(1L, 10L, "2026-08-01", "10:00", "PENDIENTE");
    }

    @Test
    void agendarCita_guardaLaCitaEnLaCapaDb() {
        citasService.agendarCita(citaDTO);

        verify(citasDbRestClient).guardarCita(citaDTO);
    }

    @Test
    void obtenerCitas_retornaLasCitasDeLaCapaDb() {
        when(citasDbRestClient.obtenerCitas()).thenReturn(List.of(citaDTO));

        List<CitaDTO> resultado = citasService.obtenerCitas();

        assertThat(resultado).containsExactly(citaDTO);
    }

    @Test
    void eliminarCita_delegaEnLaCapaDb() {
        citasService.eliminarCita(1L);

        verify(citasDbRestClient).eliminarCita(1L);
    }

    @Test
    void actualizarCita_retornaLaCitaActualizada() {
        CitaUpdateDTO update = new CitaUpdateDTO(1L, 10L, "2026-08-02", "11:00", "CONFIRMADA");
        when(citasDbRestClient.actualizarCita(update)).thenReturn(update);

        CitaUpdateDTO resultado = citasService.actualizarCita(update);

        assertThat(resultado).isEqualTo(update);
    }

    @Test
    void obtenerCitasConPacientes_enriqueceCadaCitaConSuPaciente() {
        PacienteBffDto paciente = new PacienteBffDto(10L, "11111111-1", "R1", "F1", "Juan", "Perez", "M", "1990-01-01", "Calle 1", "123456");
        when(citasDbRestClient.obtenerCitas()).thenReturn(List.of(citaDTO));
        when(pacientesBsRestClient.obtenerPaciente(10L)).thenReturn(paciente);

        List<CitaConPacienteDTO> resultado = citasService.obtenerCitasConPacientes();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(citaDTO.getId());
        assertThat(resultado.get(0).getPaciente()).isEqualTo(paciente);
    }

    @Test
    void obtenerCitasConPacientes_siFallaLaConsultaDePaciente_dejaElCampoPacienteEnNull() {
        when(citasDbRestClient.obtenerCitas()).thenReturn(List.of(citaDTO));
        when(pacientesBsRestClient.obtenerPaciente(10L)).thenThrow(new RuntimeException("paciente no encontrado"));

        List<CitaConPacienteDTO> resultado = citasService.obtenerCitasConPacientes();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPaciente()).isNull();
    }

    @Test
    void obtenerCitaConPaciente_retornaNullSiLaCitaNoExiste() {
        when(citasDbRestClient.obtenerCitas()).thenReturn(List.of(citaDTO));

        CitaConPacienteDTO resultado = citasService.obtenerCitaConPaciente(999L);

        assertThat(resultado).isNull();
    }

    @Test
    void obtenerCitaConPaciente_retornaLaCitaEnriquecidaSiExiste() {
        PacienteBffDto paciente = new PacienteBffDto(10L, "11111111-1", "R1", "F1", "Juan", "Perez", "M", "1990-01-01", "Calle 1", "123456");
        when(citasDbRestClient.obtenerCitas()).thenReturn(List.of(citaDTO));
        when(pacientesBsRestClient.obtenerPaciente(10L)).thenReturn(paciente);

        CitaConPacienteDTO resultado = citasService.obtenerCitaConPaciente(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getPaciente()).isEqualTo(paciente);
    }
}

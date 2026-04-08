package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.application.port.PdfGeneratorPort;
import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.model.Chamada;
import com.laborwaze.queue_system.domain.model.Paciente;
import com.laborwaze.queue_system.domain.model.Servico;
import com.laborwaze.queue_system.domain.model.Setor;
import com.laborwaze.queue_system.domain.repository.ChamadaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private ChamadaRepository chamadaRepository;

    @Mock
    private PdfGeneratorPort pdfGenerator;

    @InjectMocks
    private RelatorioService relatorioService;

    private Chamada chamada;

    private byte[] pdfBytes = new byte[]{'%', 'P', 'D', 'F'};

    @BeforeEach
    void setUp() {
        Paciente paciente = new Paciente("pac-001", null, null, "João Silva", "111.222.333-44", "email@valido.com", null);
        Setor setor = new Setor("setor-001", null, null, "Clínica", "Setor", true);
        Servico servico = new Servico("srv-001", null, null, "Consulta", "C", "Consulta", null, setor, true);
        chamada = new Chamada("chamada-001", null, null, "A001", paciente, servico, null, StatusChamada.AGUARDANDO, NivelPrioridade.NORMAL, LocalDateTime.of(2024, 1, 15, 10, 0), null, null);

    }

    @Test
    @DisplayName("Deve gerar relatorio de chamadas")
    void deveGerarRelatorioChamadas() throws IOException {
        when(chamadaRepository.findByDataChamadaBetween(any(), any())).thenReturn(List.of(chamada));
        doReturn(pdfBytes).when(pdfGenerator).gerarRelatorio(any(Map.class), anyList());

        byte[] result = relatorioService.gerarRelatorioChamadas(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        assertThat(result).isEqualTo(pdfBytes);
        verify(chamadaRepository).findByDataChamadaBetween(any(), any());
        verify(pdfGenerator).gerarRelatorio(
                argThat(params ->
                        params.containsKey("periodoInicio")
                                && "2024-01-01".equals(params.get("periodoInicio"))
                                && params.containsKey("total")
                                && Integer.valueOf(1).equals(params.get("total"))
                ),
                anyList()
        );
    }

    @Test
    @DisplayName("Deve gerar relatorio com lista vazia")
    void deveGerarRelatorioVazio() throws IOException {
        when(chamadaRepository.findByDataChamadaBetween(any(), any())).thenReturn(List.of());
        doReturn(pdfBytes).when(pdfGenerator).gerarRelatorio(any(Map.class), anyList());

        byte[] result = relatorioService.gerarRelatorioChamadas(LocalDate.now(), LocalDate.now());

        assertThat(result).isEqualTo(pdfBytes);
        verify(pdfGenerator).gerarRelatorio(
                argThat(params -> Integer.valueOf(0).equals(params.get("total"))),
                anyList()
        );
    }

    @Test
    @DisplayName("Deve lancar erro quando gerador de PDF falhar")
    void deveLancarErroQuandoPdfFalhar() throws IOException {
        when(chamadaRepository.findByDataChamadaBetween(any(), any())).thenReturn(List.of(chamada));
        when(pdfGenerator.gerarRelatorio(any(Map.class), anyList())).thenThrow(new IOException("Gerador indisponível"));

        assertThatThrownBy(() -> relatorioService.gerarRelatorioChamadas(LocalDate.now(), LocalDate.now()))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Gerador indisponível");
    }
}

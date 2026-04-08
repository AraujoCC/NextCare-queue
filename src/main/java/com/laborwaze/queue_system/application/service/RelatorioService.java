package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.application.port.PdfGeneratorPort;
import com.laborwaze.queue_system.domain.model.Chamada;
import com.laborwaze.queue_system.domain.repository.ChamadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final ChamadaRepository chamadaRepository;
    private final PdfGeneratorPort pdfGenerator;

    @Transactional(readOnly = true)
    public byte[] gerarRelatorioChamadas(LocalDate inicio, LocalDate fim) throws IOException {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.plusDays(1).atStartOfDay();
        List<Chamada> chamadas = chamadaRepository.findByDataChamadaBetween(inicioDateTime, fimDateTime);
        List<Map<String, Object>> dados = prepararDadosChamadas(chamadas);
        Map<String, Object> params = Map.of(
            "periodoInicio", inicio.toString(),
            "periodoFim", fim.toString(),
            "total", dados.size()
        );
        return pdfGenerator.gerarRelatorio(params, dados);
    }

    private List<Map<String, Object>> prepararDadosChamadas(List<Chamada> chamadas) {
        return chamadas.stream().map(chamada -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", chamada.getId());
            row.put("senha", chamada.getSenha());
            row.put("paciente", chamada.getPaciente().getNome());
            row.put("servico", chamada.getServico().getNome());
            row.put("status", chamada.getStatus().name());
            row.put("prioridade", chamada.getPrioridade());
            row.put("dataChamada", chamada.getDataChamada());
            row.put("dataInicio", chamada.getDataInicioAtendimento());
            row.put("dataFim", chamada.getDataFimAtendimento());
            return row;
        }).toList();
    }
}

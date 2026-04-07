package com.laborwaze.queue_system.infra.report;

import com.laborwaze.queue_system.application.port.PdfGeneratorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JasperPdfGenerator implements PdfGeneratorPort {

    @Override
    public byte[] gerarRelatorio(Map<String, Object> params, List<Map<String, Object>> dados) throws IOException {
        log.warn("PDF generation not yet implemented. Received params: {}, data count: {}",
                params != null ? params.size() : 0, dados != null ? dados.size() : 0);
        throw new UnsupportedOperationException(
                "Relatorio PDF ainda nao implementado. Configure JasperReports para habilitar.");
    }
}

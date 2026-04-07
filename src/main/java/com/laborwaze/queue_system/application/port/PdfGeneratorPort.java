package com.laborwaze.queue_system.application.port;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PdfGeneratorPort {
    byte[] gerarRelatorio(Map<String, Object> params, List<Map<String, Object>> dados) throws IOException;
}

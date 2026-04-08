package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChamadaTest {

    @Test
    void criarGeraSenha_DeveGerarSenhaCorreta_ComServico() {
        Paciente paciente = new Paciente(null, null, null, "João", "123", "joao@email.com", "999");
        Servico servico = new Servico(null, null, null, "Triagem", "TRI", "Desc", 10, new Setor(null, null, null, "Set", "Desc", true), true);

        Chamada chamada = Chamada.criarGeraSenha(paciente, servico, NivelPrioridade.NORMAL, 5);

        assertEquals("T006", chamada.getSenha());
        assertEquals(StatusChamada.AGUARDANDO, chamada.getStatus());
        assertEquals(paciente, chamada.getPaciente());
    }

    @Test
    void chamarParaAtendimento_DeveAtualizarStatusEDefinirAtendente() {
        Paciente paciente = new Paciente(null, null, null, "João", "123", "joao@email.com", "999");
        Servico servico = new Servico(null, null, null, "Triagem", "TRI", "Desc", 10, new Setor(null, null, null, "Set", "Desc", true), true);
        Chamada chamada = Chamada.criarGeraSenha(paciente, servico, NivelPrioridade.NORMAL, 0);

        Usuario atendente = new Usuario(null, null, null, "Med", "login", "senha", "email", null, null, true);

        chamada.chamarParaAtendimento(atendente);

        assertEquals(StatusChamada.EM_ATENDIMENTO, chamada.getStatus());
        assertEquals(atendente, chamada.getAtendente());
        assertNotNull(chamada.getDataInicioAtendimento());
    }

    @Test
    void finalizar_ChamadaNaoEmAtendimento_DeveLancarExcecao() {
        Paciente paciente = new Paciente(null, null, null, "João", "123", "joao@email.com", "999");
        Servico servico = new Servico(null, null, null, "Triagem", "TRI", "Desc", 10, new Setor(null, null, null, "Set", "Desc", true), true);
        Chamada chamada = Chamada.criarGeraSenha(paciente, servico, NivelPrioridade.NORMAL, 0);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, chamada::finalizar);
        assertEquals("Apenas chamadas em atendimento podem ser finalizadas.", exception.getMessage());
    }
}

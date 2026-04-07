package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.domain.model.Sala;
import com.laborwaze.queue_system.domain.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;

    @Transactional
    public Sala criar(String nome, String numero, String descricao) {
        Sala sala = Sala.builder()
                .nome(nome)
                .numero(numero)
                .descricao(descricao)
                .ativo(true)
                .build();
        return salaRepository.save(sala);
    }

    @Transactional(readOnly = true)
    public List<Sala> buscarAtivas() {
        return salaRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Sala> buscarPorId(String id) {
        return salaRepository.findById(id);
    }

    @Transactional
    public void desativar(String id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));
        sala.setAtivo(false);
        salaRepository.save(sala);
    }
}

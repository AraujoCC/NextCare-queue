package com.laborwaze.queue_system.api.response;

public record SalaResponse(
    String id,
    String nome,
    String numero,
    String descricao,
    boolean ativo
) {
    public static SalaResponse fromEntity(com.laborwaze.queue_system.domain.model.Sala s) {
        return new SalaResponse(
            s.getId(),
            s.getNome(),
            s.getNumero(),
            s.getDescricao(),
            s.getAtivo()
        );
    }
}

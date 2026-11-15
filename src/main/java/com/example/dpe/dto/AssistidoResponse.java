package com.example.dpe.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AssistidoResponse(
        UUID id,
        String nomeCompleto,
        String cpf,
        LocalDate dataNascimento,
        String telefonePrincipal,
        BigDecimal rendaMensal,
        String email,
        String nacionalidade,
        Boolean situacaoDeRua,
        EnderecoResponse endereco,
        boolean ativo,
        boolean aptoPeloCriterioDeRenda,
        LocalDateTime createdAt
) {
}

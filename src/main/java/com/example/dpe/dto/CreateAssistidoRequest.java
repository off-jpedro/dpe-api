package com.example.dpe.dto;

import com.example.dpe.validation.CPF;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@com.example.dpe.validation.EnderecoObrigatorioQuandoNaoEstiverEmSituacaoDeRua
public record CreateAssistidoRequest(

        @NotBlank
        @Size(max = 200)
        String nomeCompleto,

        @NotBlank
        @CPF
        String cpf,

        @NotNull
        LocalDate dataNascimento,

        @NotBlank
        @Size(max = 20)
        String telefonePrincipal,

        @NotNull
        @DecimalMin("0.00")
        @DecimalMax("9999999999.99")
        @Digits(integer = 10, fraction = 2)
        BigDecimal rendaMensal,

        @Email
        @Size(max = 254)
        String email,

        @NotBlank
        @Size(max = 100)
        String nacionalidade,

        @NotNull
        Boolean situacaoDeRua,

        @Valid
        EnderecoRequest endereco

) implements AssistidoEnderecoRequest {
}

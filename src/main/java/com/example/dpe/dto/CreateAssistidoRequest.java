package com.example.dpe.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateAssistidoRequest(
        @NotBlank(message = "O campo {0} Não permite null,nem texto contendo apenas espaços")
        @Size(max = 200)
        String nomeCompleto,

        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
        String cpf,

        @NotNull
        LocalDate dataNascimento,

        @NotBlank
        @Size(max = 20)
        String telefonePrincipal,

        @NotNull
        @DecimalMin(value = "0.00")
        @Digits(integer = 10, fraction = 2)
        BigDecimal rendaMensal
) {
}

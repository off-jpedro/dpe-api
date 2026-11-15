package com.example.dpe.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record EnderecoRequest(

        @Size(max = 200)
        String logradouro,

        @Size(max = 20)
        String numero,

        @Size(max = 100)
        String complemento,

        @Size(max = 100)
        String bairro,

        @Size(max = 100)
        String cidade,

        @Pattern(regexp = "^$|^[A-Za-z]{2}$",
                message = "Estado deve conter duas letras")
        String estado,

        @Pattern(regexp = "^$|^\\d{5}-?\\d{3}$",
                message = "CEP inválido")
        String cep

) {
}

package com.example.dpe.validation;

import com.example.dpe.dto.AssistidoEnderecoRequest;
import com.example.dpe.dto.EnderecoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnderecoObrigatorioValidator
        implements ConstraintValidator<
        EnderecoObrigatorioQuandoNaoEstiverEmSituacaoDeRua,
        AssistidoEnderecoRequest> {

    @Override
    public boolean isValid(
            AssistidoEnderecoRequest request,
            ConstraintValidatorContext context
    ) {
        if (request == null || request.situacaoDeRua() == null) {
            return true;
        }

        if (request.situacaoDeRua()) {
            return true;
        }

        EnderecoRequest endereco = request.endereco();

        if (endereco == null) {
            return false;
        }

        return preenchido(endereco.logradouro())
                && preenchido(endereco.numero())
                && preenchido(endereco.bairro())
                && preenchido(endereco.cidade())
                && preenchido(endereco.estado())
                && preenchido(endereco.cep());
    }

    private boolean preenchido(String valor) {
        return valor != null && !valor.isBlank();
    }
}

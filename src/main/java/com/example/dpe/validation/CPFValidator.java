package com.example.dpe.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(
            String cpf,
            ConstraintValidatorContext context
    ) {
        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        if (!cpf.matches("\\d{11}")) {
            return false;
        }

        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        int primeiroDigito = calcularDigito(cpf, 9);
        int segundoDigito = calcularDigito(cpf, 10);

        int digitosInformados = Integer.parseInt(cpf.substring(9));

        int digitosCalculados =
                primeiroDigito * 10 + segundoDigito;

        return digitosInformados == digitosCalculados;
    }

    private int calcularDigito(String cpf, int tamanho) {

        int soma = 0;
        int peso = tamanho + 1;

        for (int i = 0; i < tamanho; i++) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * peso;
            peso--;
        }

        int resto = soma % 11;

        return resto < 2 ? 0 : 11 - resto;
    }
}

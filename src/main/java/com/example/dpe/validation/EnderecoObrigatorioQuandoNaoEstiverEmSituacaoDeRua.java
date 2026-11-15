package com.example.dpe.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnderecoObrigatorioValidator.class)
public @interface EnderecoObrigatorioQuandoNaoEstiverEmSituacaoDeRua {

    String message() default
            "O endereço é obrigatório quando o assistido não está em situação de rua";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

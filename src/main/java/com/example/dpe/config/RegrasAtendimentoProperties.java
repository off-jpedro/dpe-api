package com.example.dpe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "app.regras")
public record RegrasAtendimentoProperties(
        BigDecimal salarioMinimo
) {
}

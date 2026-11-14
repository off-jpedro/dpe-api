package com.example.dpe.mapper;

import com.example.dpe.dto.AssistidoResponse;
import com.example.dpe.entity.Assistido;
import org.springframework.stereotype.Component;

@Component
public class AssistidoMapper {
    public AssistidoResponse toResponse(Assistido assistido) {
        return new AssistidoResponse(
                assistido.getId(),
                assistido.getNomeCompleto(),
                assistido.getCpf(),
                assistido.getDataNascimento(),
                assistido.getTelefonePrincipal(),
                assistido.getRendaMensal(),
                assistido.getCreatedAt()
        );
    }
}

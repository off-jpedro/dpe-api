package com.example.dpe.mapper;

import com.example.dpe.dto.AssistidoResponse;
import com.example.dpe.dto.EnderecoRequest;
import com.example.dpe.dto.EnderecoResponse;
import com.example.dpe.entity.Assistido;
import com.example.dpe.entity.Endereco;
import org.springframework.stereotype.Component;

@Component
public class AssistidoMapper {

    public Endereco toEndereco(EnderecoRequest request) {

        if (request == null) {
            return null;
        }

        return new Endereco(
                request.logradouro(),
                request.numero(),
                request.complemento(),
                request.bairro(),
                request.cidade(),
                request.estado(),
                request.cep()
        );
    }

    public EnderecoResponse toEnderecoResponse(Endereco endereco) {

        if (endereco == null) {
            return null;
        }

        return new EnderecoResponse(
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }

    public AssistidoResponse toResponse(
            Assistido assistido,
            boolean aptoPeloCriterioDeRenda
    ) {
        return new AssistidoResponse(
                assistido.getId(),
                assistido.getNomeCompleto(),
                assistido.getCpf(),
                assistido.getDataNascimento(),
                assistido.getTelefonePrincipal(),
                assistido.getRendaMensal(),
                assistido.getEmail(),
                assistido.getNacionalidade(),
                assistido.getSituacaoDeRua(),
                toEnderecoResponse(assistido.getEndereco()),
                assistido.isAtivo(),
                aptoPeloCriterioDeRenda,
                assistido.getCreatedAt()
        );
    }
}

package com.example.dpe.service;

import com.example.dpe.dto.CreateAssistidoRequest;
import com.example.dpe.dto.UpdateAssistidoRequest;
import com.example.dpe.entity.Assistido;
import com.example.dpe.exception.AssistidoNaoEncontradoException;
import com.example.dpe.exception.CpfJaCadastradoException;
import com.example.dpe.repository.AssistidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AssistidoService {

    private final AssistidoRepository repository;

    public AssistidoService(AssistidoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Assistido createAssistido(CreateAssistidoRequest request) {

        if (repository.existsByCpf(request.cpf())) {
            throw new CpfJaCadastradoException(
                    "Já existe um assistido cadastrado com este CPF."
            );
        }

        Assistido assistido = new Assistido(
                request.nomeCompleto(),
                request.cpf(),
                request.dataNascimento(),
                request.telefonePrincipal(),
                request.rendaMensal()
        );

        return repository.save(assistido);
    }

    public Assistido findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new AssistidoNaoEncontradoException(
                                "Assistido não encontrado."
                        )
                );
    }

    public List<Assistido> findAll() {
        return repository.findAll();
    }

    public Assistido updateAssistido(
            UUID id,
            UpdateAssistidoRequest request
    ) {
        Assistido assistido = findById(id);
        if (!assistido.getCpf().equals(request.cpf())
                && repository.existsByCpf(request.cpf())) {
            throw new CpfJaCadastradoException(
                    "Já existe um assistido cadastrado com este CPF."
            );
        }
        assistido.setNomeCompleto(request.nomeCompleto());
        assistido.setCpf(request.cpf());
        assistido.setDataNascimento(request.dataNascimento());
        assistido.setTelefonePrincipal(request.telefonePrincipal());
        assistido.setRendaMensal(request.rendaMensal());
        return repository.save(assistido);
    }

    public void deleteAssistido(UUID id) {
        var assistido = findById(id);
        repository.delete(assistido);
    }
}

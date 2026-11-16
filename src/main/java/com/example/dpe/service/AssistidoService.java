package com.example.dpe.service;

import com.example.dpe.config.RegrasAtendimentoProperties;
import com.example.dpe.dto.AssistidoResponse;
import com.example.dpe.dto.CreateAssistidoRequest;
import com.example.dpe.dto.EnderecoResponse;
import com.example.dpe.dto.UpdateAssistidoRequest;
import com.example.dpe.entity.Assistido;
import com.example.dpe.exception.AssistidoNaoEncontradoException;
import com.example.dpe.exception.CpfJaCadastradoException;
import com.example.dpe.mapper.AssistidoMapper;
import com.example.dpe.repository.AssistidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AssistidoService {

    private final AssistidoRepository repository;
    private final AssistidoMapper mapper;
    private final RegrasAtendimentoProperties regrasAtendimentoProperties;

    public AssistidoService(
            AssistidoRepository repository,
            AssistidoMapper mapper,
            RegrasAtendimentoProperties regrasAtendimentoProperties
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.regrasAtendimentoProperties = regrasAtendimentoProperties;
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
                request.rendaMensal(),
                request.email(),
                request.nacionalidade(),
                request.situacaoDeRua(),
                mapper.toEndereco(request.endereco())
        );

        return repository.save(assistido);
    }

    public Assistido findById(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() ->
                        new AssistidoNaoEncontradoException(
                                "Assistido não encontrado."
                        )
                );
    }

    public Page<Assistido> buscarAssistidos(
            String nome,
            String cpf,
            String email,
            String nacionalidade,
            String cidade,
            Boolean situacaoDeRua,
            Pageable pageable
    ) {
        return repository.buscarAssistidos(
                normalizarFiltro(nome),
                normalizarFiltro(cpf),
                normalizarFiltro(email),
                normalizarFiltro(nacionalidade),
                normalizarFiltro(cidade),
                situacaoDeRua,
                pageable
        );
    }


    public Page<Assistido> buscarAssistidosInativos(
            String nome,
            String cpf,
            String email,
            String nacionalidade,
            String cidade,
            Boolean situacaoDeRua,
            Pageable pageable
    ) {
        return repository.buscarAssistidosInativos(
                normalizarFiltro(nome),
                normalizarFiltro(cpf),
                normalizarFiltro(email),
                normalizarFiltro(nacionalidade),
                normalizarFiltro(cidade),
                situacaoDeRua,
                pageable
        );
    }

    private AssistidoResponse toResponse(Assistido assistido) {
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
                assistido.getEndereco() != null
                        ? new EnderecoResponse(
                        assistido.getEndereco().getLogradouro(),
                        assistido.getEndereco().getNumero(),
                        assistido.getEndereco().getComplemento(),
                        assistido.getEndereco().getBairro(),
                        assistido.getEndereco().getCidade(),
                        assistido.getEndereco().getEstado(),
                        assistido.getEndereco().getCep()
                )
                        : null,
                assistido.isAtivo(),
                isAptoPeloCriterioDeRenda(assistido.getRendaMensal()),
                assistido.getCreatedAt()
        );
    }

    public Page<AssistidoResponse> buscarTodosAssistidos(
            String nome,
            String cpf,
            String email,
            String nacionalidade,
            String cidade,
            Boolean situacaoDeRua,
            Pageable pageable
    ) {
        Page<Assistido> assistidos = repository.buscarTodosAssistidos(
                normalizarFiltro(nome),
                normalizarFiltro(cpf),
                normalizarFiltro(email),
                normalizarFiltro(nacionalidade),
                normalizarFiltro(cidade),
                situacaoDeRua,
                pageable
        );

        return assistidos.map(this::toResponse);
    }


    private String normalizarFiltro(String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return "";
        }

        return filtro.trim().toLowerCase();
    }

    @Transactional
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
        assistido.setEmail(request.email());
        assistido.setNacionalidade(request.nacionalidade());
        assistido.setSituacaoDeRua(request.situacaoDeRua());
        assistido.setEndereco(mapper.toEndereco(request.endereco()));

        return repository.save(assistido);
    }

    @Transactional
    public void deleteAssistido(UUID id) {
        Assistido assistido = findById(id);

        assistido.desativar();

        repository.save(assistido);
    }


    @Transactional
    public void excluirPermanentemente(UUID id) {
        Assistido assistido = repository.findByIdAndAtivoFalse(id)
                .orElseThrow(() ->
                        new AssistidoNaoEncontradoException(
                                "Assistido inativo não encontrado."
                        )
                );

        repository.delete(assistido);
    }


    public boolean isAptoPeloCriterioDeRenda(BigDecimal rendaMensal) {

        BigDecimal limite = regrasAtendimentoProperties
                .salarioMinimo()
                .multiply(BigDecimal.valueOf(2));

        return rendaMensal.compareTo(limite) <= 0;
    }
}

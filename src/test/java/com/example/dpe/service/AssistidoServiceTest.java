package com.example.dpe.service;

import com.example.dpe.config.RegrasAtendimentoProperties;
import com.example.dpe.dto.CreateAssistidoRequest;
import com.example.dpe.dto.EnderecoRequest;
import com.example.dpe.dto.UpdateAssistidoRequest;
import com.example.dpe.entity.Assistido;
import com.example.dpe.exception.AssistidoNaoEncontradoException;
import com.example.dpe.exception.CpfJaCadastradoException;
import com.example.dpe.mapper.AssistidoMapper;
import com.example.dpe.repository.AssistidoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssistidoServiceTest {

    @Mock
    private AssistidoRepository repository;

    private AssistidoService service;

    private final AssistidoMapper mapper = new AssistidoMapper();

    private final RegrasAtendimentoProperties regras =
            new RegrasAtendimentoProperties(new BigDecimal("1621.00"));

    @BeforeEach
    void setUp() {
        service = new AssistidoService(repository, mapper, regras);
    }

    @Test
    void shouldCreateAssistidoSuccessfully() {
        // Arrange
        CreateAssistidoRequest request = criarCreateRequest();

        when(repository.existsByCpf(request.cpf()))
                .thenReturn(false);

        when(repository.save(any(Assistido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Assistido result = service.createAssistido(request);

        // Assert
        assertNotNull(result);
        assertEquals("João da Silva", result.getNomeCompleto());
        assertEquals("12345678900", result.getCpf());
        assertEquals("joao@email.com", result.getEmail());
        assertEquals("Brasileira", result.getNacionalidade());
        assertFalse(result.getSituacaoDeRua());
        assertNotNull(result.getEndereco());

        verify(repository).existsByCpf(request.cpf());
        verify(repository).save(any(Assistido.class));
    }

    @Test
    void shouldNotCreateAssistidoWhenCpfAlreadyExists() {
        // Arrange
        CreateAssistidoRequest request = criarCreateRequest();

        when(repository.existsByCpf(request.cpf()))
                .thenReturn(true);

        // Act + Assert
        CpfJaCadastradoException exception = assertThrows(
                CpfJaCadastradoException.class,
                () -> service.createAssistido(request)
        );

        assertEquals(
                "Já existe um assistido cadastrado com este CPF.",
                exception.getMessage()
        );

        verify(repository).existsByCpf(request.cpf());
        verify(repository, never()).save(any(Assistido.class));
    }

    @Test
    void shouldFindAssistidoByIdSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido();

        when(repository.findByIdAndAtivoTrue(id))
                .thenReturn(Optional.of(assistido));

        // Act
        Assistido result = service.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals("João da Silva", result.getNomeCompleto());
        assertEquals("12345678910", result.getCpf());

        verify(repository).findByIdAndAtivoTrue(id);
    }

    @Test
    void shouldThrowExceptionWhenAssistidoDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(repository.findByIdAndAtivoTrue(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                AssistidoNaoEncontradoException.class,
                () -> service.findById(id)
        );

        verify(repository).findByIdAndAtivoTrue(id);
    }

    @Test
    void shouldFindAssistidosSuccessfully() {
        // Arrange
        Assistido assistido1 = criarAssistido();

        Assistido assistido2 = new Assistido(
                "Maria Oliveira",
                "98765432100",
                LocalDate.of(1995, 5, 10),
                "62988888888",
                BigDecimal.valueOf(2000)
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<Assistido> page = new PageImpl<>(
                List.of(assistido1, assistido2),
                pageable,
                2
        );

        when(repository.buscarAssistidos(
                "",
                "",
                "",
                "",
                "",
                null,
                pageable
        )).thenReturn(page);

        // Act
        Page<Assistido> result = service.buscarAssistidos(
                null, null, null, null, null, null, pageable
        );

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("João da Silva", result.getContent().get(0).getNomeCompleto());
        assertEquals("Maria Oliveira", result.getContent().get(1).getNomeCompleto());

        verify(repository).buscarAssistidos(
                "",
                "",
                "",
                "",
                "",
                null,
                pageable
        );
    }

    @Test
    void shouldReturnEmptyPageWhenNoAssistidosExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.buscarAssistidos(
                "",
                "",
                "",
                "",
                "",
                null,
                pageable
        )).thenReturn(Page.empty());

        // Act
        Page<Assistido> result = service.buscarAssistidos(
                null, null, null, null, null, null, pageable
        );

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(repository).buscarAssistidos(
                "",
                "",
                "",
                "",
                "",
                null,
                pageable
        );
    }

    @Test
    void shouldUpdateAssistidoSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido();

        UpdateAssistidoRequest request = criarUpdateRequest(
                "João Atualizado",
                "12345678910"
        );

        when(repository.findByIdAndAtivoTrue(id))
                .thenReturn(Optional.of(assistido));

        when(repository.save(any(Assistido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Assistido result = service.updateAssistido(id, request);

        // Assert
        assertNotNull(result);
        assertEquals("João Atualizado", result.getNomeCompleto());
        assertEquals("12345678910", result.getCpf());
        assertEquals("62977777777", result.getTelefonePrincipal());
        assertEquals(BigDecimal.valueOf(2500), result.getRendaMensal());
        assertEquals("joao.novo@email.com", result.getEmail());

        verify(repository).findByIdAndAtivoTrue(id);
        verify(repository, never()).existsByCpf(any());
        verify(repository).save(any(Assistido.class));
    }

    @Test
    void shouldNotUpdateAssistidoWhenCpfAlreadyExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido();

        UpdateAssistidoRequest request = criarUpdateRequest(
                "João Atualizado",
                "98765432100"
        );

        when(repository.findByIdAndAtivoTrue(id))
                .thenReturn(Optional.of(assistido));

        when(repository.existsByCpf(request.cpf()))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                CpfJaCadastradoException.class,
                () -> service.updateAssistido(id, request)
        );

        verify(repository).findByIdAndAtivoTrue(id);
        verify(repository).existsByCpf(request.cpf());
        verify(repository, never()).save(any(Assistido.class));
    }

    @Test
    void shouldNotUpdateAssistidoWhenIdDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();

        UpdateAssistidoRequest request = criarUpdateRequest(
                "João Atualizado",
                "98765432100"
        );

        when(repository.findByIdAndAtivoTrue(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                AssistidoNaoEncontradoException.class,
                () -> service.updateAssistido(id, request)
        );

        verify(repository).findByIdAndAtivoTrue(id);
        verify(repository, never()).save(any(Assistido.class));
    }

    @Test
    void shouldDeleteAssistidoSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido();

        when(repository.findByIdAndAtivoTrue(id))
                .thenReturn(Optional.of(assistido));

        when(repository.save(any(Assistido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        service.deleteAssistido(id);

        // Assert
        assertFalse(assistido.isAtivo());

        verify(repository).findByIdAndAtivoTrue(id);
        verify(repository).save(assistido);
        verify(repository, never()).delete(any(Assistido.class));
    }

    @Test
    void shouldNotDeleteAssistidoWhenIdDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(repository.findByIdAndAtivoTrue(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                AssistidoNaoEncontradoException.class,
                () -> service.deleteAssistido(id)
        );

        verify(repository).findByIdAndAtivoTrue(id);
        verify(repository, never()).save(any(Assistido.class));
    }

    private CreateAssistidoRequest criarCreateRequest() {
        return new CreateAssistidoRequest(
                "João da Silva",
                "12345678900",
                LocalDate.of(1995, 5, 10),
                "62999999999",
                new BigDecimal("1500.00"),
                "joao@email.com",
                "Brasileira",
                false,
                criarEnderecoRequest()
        );
    }

    private UpdateAssistidoRequest criarUpdateRequest(
            String nome,
            String cpf
    ) {
        return new UpdateAssistidoRequest(
                nome,
                cpf,
                LocalDate.of(1999, 1, 2),
                "62977777777",
                BigDecimal.valueOf(2500),
                "joao.novo@email.com",
                "Brasileira",
                false,
                criarEnderecoRequest()
        );
    }

    private EnderecoRequest criarEnderecoRequest() {
        return new EnderecoRequest(
                "Rua das Flores",
                "100",
                null,
                "Centro",
                "Goiânia",
                "GO",
                "74000000"
        );
    }

    private Assistido criarAssistido() {
        return new Assistido(
                "João da Silva",
                "12345678910",
                LocalDate.of(1999, 1, 2),
                "62999999999",
                BigDecimal.valueOf(1500)
        );
    }
}

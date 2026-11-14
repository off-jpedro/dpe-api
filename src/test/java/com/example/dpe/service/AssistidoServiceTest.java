package com.example.dpe.service;

import com.example.dpe.dto.CreateAssistidoRequest;
import com.example.dpe.dto.UpdateAssistidoRequest;
import com.example.dpe.entity.Assistido;
import com.example.dpe.exception.AssistidoNaoEncontradoException;
import com.example.dpe.exception.CpfJaCadastradoException;
import com.example.dpe.repository.AssistidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssistidoServiceTest {

    @Mock
    private AssistidoRepository repository;

    @InjectMocks
    private AssistidoService service;

    @Test
    void shouldCreateAssistidoSuccessfully(){
        //Arrange
        CreateAssistidoRequest assistidoRequest = new CreateAssistidoRequest(
                "Name Teste",
                "12335578910",
                LocalDate.ofYearDay(1999,2),
                "62999999999",
                new BigDecimal(1500)
        );
        when(repository.existsByCpf(assistidoRequest.cpf()))
                .thenReturn(false);

        when(repository.save(any(Assistido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Assistido result = service.createAssistido(assistidoRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Name Teste", result.getNomeCompleto());
        assertEquals("12335578910", result.getCpf());

        verify(repository).existsByCpf(assistidoRequest.cpf());
        verify(repository).save(any(Assistido.class));
    }

    @Test
    void shouldNotCreateAssistidoWhenCpfAlreadyExists(){
        //Arrange
        CreateAssistidoRequest assistidoRequest = new CreateAssistidoRequest(
                "Name Teste",
                "12335578910",
                LocalDate.ofYearDay(1999,2),
                "62999999999",
                new BigDecimal(1500)
        );
        when(repository.existsByCpf(assistidoRequest.cpf()))
                .thenReturn(true);

        // Act + Assert
        CpfJaCadastradoException exception = assertThrows(
                CpfJaCadastradoException.class,
                () -> service.createAssistido(assistidoRequest)
        );

        assertEquals(
                "Já existe um assistido cadastrado com este CPF.",
                exception.getMessage()
        );

        verify(repository).existsByCpf(assistidoRequest.cpf());
        verify(repository, never()).save(any(Assistido.class));
    }

    @Test
    void shouldFindAssistidoByIdSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido();

        when(repository.findById(id))
                .thenReturn(Optional.of(assistido));

        // Act
        Assistido result = service.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals("João da Silva", result.getNomeCompleto());
        assertEquals("12345678910", result.getCpf());

        verify(repository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenAssistidoDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                AssistidoNaoEncontradoException.class,
                () -> service.findById(id)
        );

        verify(repository).findById(id);
    }

    @Test
    void shouldFindAllAssistidosSuccessfully() {
        // Arrange
        Assistido assistido1 = criarAssistido();

        Assistido assistido2 = new Assistido(
                "Maria Oliveira",
                "98765432100",
                LocalDate.of(1995, 5, 10),
                "62988888888",
                BigDecimal.valueOf(2000)
        );

        when(repository.findAll())
                .thenReturn(List.of(assistido1, assistido2));

        // Act
        List<Assistido> result = service.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("João da Silva", result.get(0).getNomeCompleto());
        assertEquals("Maria Oliveira", result.get(1).getNomeCompleto());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoAssistidosExist() {
        // Arrange
        when(repository.findAll())
                .thenReturn(List.of());

        // Act
        List<Assistido> result = service.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void shouldUpdateAssistidoSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido();

        UpdateAssistidoRequest request = new UpdateAssistidoRequest(
                "João Atualizado",
                "12345678910",
                LocalDate.of(1999, 1, 2),
                "62977777777",
                BigDecimal.valueOf(2500)
        );

        when(repository.findById(id))
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

        verify(repository).findById(id);
        verify(repository, never()).existsByCpf(any());
        verify(repository).save(any(Assistido.class));
    }

    @Test
    void shouldNotUpdateAssistidoWhenCpfAlreadyExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido();

        UpdateAssistidoRequest request = new UpdateAssistidoRequest(
                "João Atualizado",
                "98765432100",
                LocalDate.of(1999, 1, 2),
                "62977777777",
                BigDecimal.valueOf(2500)
        );

        when(repository.findById(id))
                .thenReturn(Optional.of(assistido));

        when(repository.existsByCpf(request.cpf()))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                CpfJaCadastradoException.class,
                () -> service.updateAssistido(id, request)
        );

        verify(repository).findById(id);
        verify(repository).existsByCpf(request.cpf());
        verify(repository, never()).save(any(Assistido.class));
    }

    @Test
    void shouldNotUpdateAssistidoWhenIdDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();

        UpdateAssistidoRequest request = new UpdateAssistidoRequest(
                "João Atualizado",
                "98765432100",
                LocalDate.of(1999, 1, 2),
                "62977777777",
                BigDecimal.valueOf(2500)
        );

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                AssistidoNaoEncontradoException.class,
                () -> service.updateAssistido(id, request)
        );

        verify(repository).findById(id);
        verify(repository, never()).save(any(Assistido.class));
    }

    @Test
    void shouldDeleteAssistidoSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido();

        when(repository.findById(id))
                .thenReturn(Optional.of(assistido));

        // Act
        service.deleteAssistido(id);

        // Assert
        verify(repository).findById(id);
        verify(repository).delete(assistido);
    }

    @Test
    void shouldNotDeleteAssistidoWhenIdDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                AssistidoNaoEncontradoException.class,
                () -> service.deleteAssistido(id)
        );

        verify(repository).findById(id);
        verify(repository, never()).delete(any(Assistido.class));
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

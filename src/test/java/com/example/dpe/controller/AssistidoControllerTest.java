package com.example.dpe.controller;

import com.example.dpe.dto.CreateAssistidoRequest;
import com.example.dpe.dto.UpdateAssistidoRequest;
import com.example.dpe.entity.Assistido;
import com.example.dpe.exception.AssistidoNaoEncontradoException;
import com.example.dpe.exception.CpfJaCadastradoException;
import com.example.dpe.mapper.AssistidoMapper;
import com.example.dpe.service.AssistidoService;
import com.example.dpe.exception.GlobalExceptionHandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AssistidoController.class)
@Import({AssistidoMapper.class, GlobalExceptionHandler.class})
class AssistidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssistidoService service;

    private Assistido criarAssistido(UUID id) {
        Assistido assistido = new Assistido(
                "João da Silva",
                "52998224725",
                LocalDate.of(1999, 1, 2),
                "62999999999",
                BigDecimal.valueOf(1500)
        );

        ReflectionTestUtils.setField(assistido, "id", id);
        ReflectionTestUtils.setField(
                assistido,
                "createdAt",
                LocalDateTime.of(2026, 11, 14, 10, 0)
        );

        return assistido;
    }


    @Test
    void shouldCreateAssistidoSuccessfully() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido(id);

        when(service.createAssistido(any(CreateAssistidoRequest.class)))
                .thenReturn(assistido);

        String requestJson = """
        {
            "nomeCompleto": "João da Silva",
            "cpf": "52998224725",
            "dataNascimento": "1999-01-02",
            "telefonePrincipal": "62999999999",
            "rendaMensal": 1500,
            "email": "joao@email.com",
            "nacionalidade": "Brasileira",
            "situacaoDeRua": false,
            "endereco": {
                "logradouro": "Rua das Flores",
                "numero": "100",
                "complemento": null,
                "bairro": "Centro",
                "cidade": "Goiânia",
                "estado": "GO",
                "cep": "74000000"
            }
        }
        """;

        // Act + Assert
        mockMvc.perform(post("/api/assistidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nomeCompleto").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("52998224725"));

        verify(service).createAssistido(any(CreateAssistidoRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingAssistidoWithInvalidData()
            throws Exception {

        // Arrange
        String requestJson = """
                {
                    "nomeCompleto": "",
                    "cpf": "123",
                    "dataNascimento": "1999-01-02",
                    "telefonePrincipal": "62999999999",
                    "rendaMensal": 1500
                }
                """;

        // Act + Assert
        mockMvc.perform(post("/api/assistidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .createAssistido(any(CreateAssistidoRequest.class));
    }

    @Test
    void shouldReturnConflictWhenCpfAlreadyExists() throws Exception {
        // Arrange
        when(service.createAssistido(any(CreateAssistidoRequest.class)))
                .thenThrow(new CpfJaCadastradoException(
                        "Já existe um assistido cadastrado com este CPF."
                ));

        String requestJson = """
        {
            "nomeCompleto": "João Atualizado",
            "cpf": "52998224725",
            "dataNascimento": "1999-01-02",
            "telefonePrincipal": "62977777777",
            "rendaMensal": 2500,
            "email": "joao.novo@email.com",
            "nacionalidade": "Brasileira",
            "situacaoDeRua": false,
            "endereco": {
                "logradouro": "Rua das Flores",
                "numero": "100",
                "complemento": null,
                "bairro": "Centro",
                "cidade": "Goiânia",
                "estado": "GO",
                "cep": "74000000"
            }
        }
        """;

        // Act + Assert
        mockMvc.perform(post("/api/assistidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldFindAssistidoByIdSuccessfully() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        Assistido assistido = criarAssistido(id);

        when(service.findById(id)).thenReturn(assistido);

        // Act + Assert
        mockMvc.perform(get("/api/assistidos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nomeCompleto").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("52998224725"));

        verify(service).findById(id);
    }

    @Test
    void shouldReturnNotFoundWhenAssistidoDoesNotExist() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();

        when(service.findById(id))
                .thenThrow(new AssistidoNaoEncontradoException(
                        "Assistido não encontrado."
                ));

        // Act + Assert
        mockMvc.perform(get("/api/assistidos/{id}", id))
                .andExpect(status().isNotFound());

        verify(service).findById(id);
    }

    @Test
    void shouldFindAllAssistidosSuccessfully() throws Exception {
        // Arrange
        Assistido assistido1 = criarAssistido(UUID.randomUUID());

        Assistido assistido2 = new Assistido(
                "Maria Oliveira",
                "98765432100",
                LocalDate.of(1995, 5, 10),
                "62988888888",
                BigDecimal.valueOf(2000)
        );

        when(service.buscarAssistidos(
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(Boolean.class),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(assistido1, assistido2)));

        // Act + Assert
        mockMvc.perform(get("/api/assistidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].nomeCompleto")
                        .value("João da Silva"))
                .andExpect(jsonPath("$.content[1].nomeCompleto")
                        .value("Maria Oliveira"));

        verify(service).buscarAssistidos(
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(Boolean.class),
                any(Pageable.class)
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoAssistidosExist() throws Exception {
        // Arrange
        when(service.buscarAssistidos(
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(Boolean.class),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of()));

        // Act + Assert
        mockMvc.perform(get("/api/assistidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));

        verify(service).buscarAssistidos(
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(Boolean.class),
                any(Pageable.class)
        );
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentAssistido()
            throws Exception {

        // Arrange
        UUID id = UUID.randomUUID();

        when(service.updateAssistido(
                eq(id),
                any(UpdateAssistidoRequest.class)
        )).thenThrow(new AssistidoNaoEncontradoException(
                "Assistido não encontrado."
        ));

        String requestJson = """
        {
            "nomeCompleto": "João Atualizado",
            "cpf": "52998224725",
            "dataNascimento": "1999-01-02",
            "telefonePrincipal": "62977777777",
            "rendaMensal": 2500,
            "email": "joao.novo@email.com",
            "nacionalidade": "Brasileira",
            "situacaoDeRua": false,
            "endereco": {
                "logradouro": "Rua das Flores",
                "numero": "100",
                "complemento": null,
                "bairro": "Centro",
                "cidade": "Goiânia",
                "estado": "GO",
                "cep": "74000000"
            }
        }
        """;

        // Act + Assert
        mockMvc.perform(put("/api/assistidos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAssistidoSuccessfully() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();

        doNothing().when(service).deleteAssistido(id);

        // Act + Assert
        mockMvc.perform(delete("/api/assistidos/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(service).deleteAssistido(id);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentAssistido()
            throws Exception {

        // Arrange
        UUID id = UUID.randomUUID();

        doThrow(new AssistidoNaoEncontradoException(
                "Assistido não encontrado."
        )).when(service).deleteAssistido(id);

        // Act + Assert
        mockMvc.perform(delete("/api/assistidos/{id}", id))
                .andExpect(status().isNotFound());

        verify(service).deleteAssistido(id);
    }


    @Test
    void shouldReturnBadRequestWhenCreatingAssistidoWithInvalidCPF()
            throws Exception {

        // Arrange
        String requestJson = """
        {
            "nomeCompleto": "João da Silva",
            "cpf": "12345678910",
            "dataNascimento": "1999-01-02",
            "telefonePrincipal": "62999999999",
            "rendaMensal": 1500,
            "email": "joao@email.com",
            "nacionalidade": "Brasileira",
            "situacaoDeRua": false,
            "endereco": {
                "logradouro": "Rua das Flores",
                "numero": "100",
                "complemento": null,
                "bairro": "Centro",
                "cidade": "Goiânia",
                "estado": "GO",
                "cep": "74000000"
            }
        }
        """;

        // Act + Assert
        mockMvc.perform(post("/api/assistidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verify(service, never())
                .createAssistido(any(CreateAssistidoRequest.class));
    }

}
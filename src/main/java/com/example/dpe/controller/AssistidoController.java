package com.example.dpe.controller;

import com.example.dpe.dto.AssistidoResponse;
import com.example.dpe.dto.CreateAssistidoRequest;
import com.example.dpe.dto.UpdateAssistidoRequest;
import com.example.dpe.entity.Assistido;
import com.example.dpe.mapper.AssistidoMapper;
import com.example.dpe.service.AssistidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/assistidos")
public class AssistidoController {

    private final AssistidoService service;
    private final AssistidoMapper mapper;

    public AssistidoController(
            AssistidoService service,
            AssistidoMapper mapper
    ) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<AssistidoResponse> createAssistido(
            @RequestBody @Valid CreateAssistidoRequest request
    ) {
        Assistido assistido = service.createAssistido(request);

        AssistidoResponse response = mapper.toResponse(
                assistido,
                service.isAptoPeloCriterioDeRenda(assistido.getRendaMensal())
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(assistido.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssistidoResponse> findById(
            @PathVariable UUID id
    ) {
        Assistido assistido = service.findById(id);

        AssistidoResponse response = mapper.toResponse(
                assistido,
                service.isAptoPeloCriterioDeRenda(assistido.getRendaMensal())
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<AssistidoResponse>> buscarAssistidos(

            @RequestParam(required = false) String nome,

            @RequestParam(required = false) String cpf,

            @RequestParam(required = false) String email,

            @RequestParam(required = false) String nacionalidade,

            @RequestParam(required = false) String cidade,

            @RequestParam(required = false) Boolean situacaoDeRua,

            @PageableDefault(
                    size = 10,
                    sort = "nomeCompleto",
                    direction = Sort.Direction.ASC
            ) Pageable pageable

    ) {
        Page<Assistido> pagina = service.buscarAssistidos(
                nome,
                cpf,
                email,
                nacionalidade,
                cidade,
                situacaoDeRua,
                pageable
        );

        Page<AssistidoResponse> response = pagina.map(assistido ->
                mapper.toResponse(
                        assistido,
                        service.isAptoPeloCriterioDeRenda(
                                assistido.getRendaMensal()
                        )
                )
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/inativos")
    public ResponseEntity<Page<AssistidoResponse>> buscarAssistidosInativos(

            @RequestParam(required = false) String nome,

            @RequestParam(required = false) String cpf,

            @RequestParam(required = false) String email,

            @RequestParam(required = false) String nacionalidade,

            @RequestParam(required = false) String cidade,

            @RequestParam(required = false) Boolean situacaoDeRua,

            @PageableDefault(
                    size = 10,
                    sort = "nomeCompleto",
                    direction = Sort.Direction.ASC
            ) Pageable pageable

    ) {
        Page<Assistido> pagina = service.buscarAssistidosInativos(
                nome,
                cpf,
                email,
                nacionalidade,
                cidade,
                situacaoDeRua,
                pageable
        );

        Page<AssistidoResponse> response = pagina.map(assistido ->
                mapper.toResponse(
                        assistido,
                        service.isAptoPeloCriterioDeRenda(
                                assistido.getRendaMensal()
                        )
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/todos")
    public Page<AssistidoResponse> buscarTodosAssistidos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nacionalidade,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) Boolean situacaoDeRua,
            @PageableDefault(
                    size = 10,
                    sort = "nomeCompleto",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return service.buscarTodosAssistidos(
                nome,
                cpf,
                email,
                nacionalidade,
                cidade,
                situacaoDeRua,
                pageable
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssistidoResponse> updateAssistido(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateAssistidoRequest request
    ) {
        Assistido assistido = service.updateAssistido(id, request);

        AssistidoResponse response = mapper.toResponse(
                assistido,
                service.isAptoPeloCriterioDeRenda(assistido.getRendaMensal())
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssistido(
            @PathVariable UUID id
    ) {
        service.deleteAssistido(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Void> excluirPermanentemente(
            @PathVariable UUID id
    ) {
        service.excluirPermanentemente(id);

        return ResponseEntity.noContent().build();
    }

}

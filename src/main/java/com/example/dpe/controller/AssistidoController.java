package com.example.dpe.controller;

import com.example.dpe.dto.AssistidoResponse;
import com.example.dpe.dto.CreateAssistidoRequest;
import com.example.dpe.dto.UpdateAssistidoRequest;
import com.example.dpe.entity.Assistido;
import com.example.dpe.mapper.AssistidoMapper;
import com.example.dpe.service.AssistidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
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

        AssistidoResponse response = mapper.toResponse(assistido);

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
             AssistidoResponse response = mapper.toResponse(assistido);
             return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AssistidoResponse>> findAll() {
        List<Assistido> assistidos = service.findAll();
        List<AssistidoResponse> response = assistidos.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssistidoResponse> updateAssistido(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateAssistidoRequest request
    ) {
        Assistido assistido = service.updateAssistido(id, request);

        AssistidoResponse response = mapper.toResponse(assistido);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssistido(
            @PathVariable UUID id
    ) {
        service.deleteAssistido(id);

        return ResponseEntity.noContent().build();
    }
}

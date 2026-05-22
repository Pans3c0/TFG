package org.educastur.samuelepv59.todo_list.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.dtos.EtiquetaRequest;
import org.educastur.samuelepv59.todo_list.dtos.EtiquetaResponse;
import org.educastur.samuelepv59.todo_list.services.EtiquetaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/etiquetas")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name = "Tags", description = "Gestión de las etiquetas para tareas")
public class EtiquetaController {

    private final EtiquetaService tagService;

    @Operation(summary = "Listar todas las etiquetas", description = "Devuelve una lista con todas las etiquetas")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<EtiquetaResponse> getAll() {
        return tagService.getAll();
    }

    @Operation(summary = "Obtener etiqueta por ID", description = "Busca una etiqueta específica")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public EtiquetaResponse obtenerPorId(@Parameter(description = "ID de la etiqueta") @PathVariable Long id) {
        return tagService.obtenerPorId(id);
    }

    @Operation(summary = "Crear etiqueta", description = "Registra una nueva etiqueta")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<EtiquetaResponse> crear(@Valid @RequestBody EtiquetaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.crear(request));
    }

    @Operation(summary = "Actualizar etiqueta", description = "Modifica los detalles de una etiqueta")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public EtiquetaResponse actualizar(
            @Parameter(description = "ID de la etiqueta") @PathVariable Long id,
            @Valid @RequestBody EtiquetaRequest request) {
        return tagService.actualizar(id, request);
    }

    @Operation(summary = "Eliminar etiqueta", description = "Borra una etiqueta permanentemente")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void eliminar(@Parameter(description = "ID de la etiqueta") @PathVariable Long id) {
        tagService.eliminar(id);
    }
}

package org.educastur.samuelepv59.todo_list.controllers;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.educastur.samuelepv59.todo_list.dtos.CategoriaRequest;
import org.educastur.samuelepv59.todo_list.dtos.CategoriaResponse;
import org.educastur.samuelepv59.todo_list.services.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Gestión de las categorías de tareas")
public class CategoriaController {
    private final CategoriaService categoryService;

    @Operation(summary = "Listar todas las categorías", description = "Devuelve una lista con todas las categorías registradas")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<CategoriaResponse> getAll() {
        return categoryService.listarTodas();
    }

    @Operation(summary = "Obtener categoría por ID", description = "Busca una categoría específica")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public CategoriaResponse obtenerPorId(@Parameter(description = "ID de la categoría") @PathVariable Long id) {
        return categoryService.obtenerPorId(id);
    }

    @Operation(summary = "Crear nueva categoría", description = "Crea una categoría en el sistema")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.crear(request));
    }

    @Operation(summary = "Actualizar categoría", description = "Modifica los detalles de una categoría existente")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    @PutMapping("/{id}")
    public CategoriaResponse actualizar(
            @Parameter(description = "ID de la categoría") @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {
        return categoryService.actualizar(id, request);
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría permanentemente")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    @DeleteMapping("/{id}")
    public void eliminar(@Parameter(description = "ID de la categoría") @PathVariable Long id) {
        categoryService.eliminar(id);
    }
}

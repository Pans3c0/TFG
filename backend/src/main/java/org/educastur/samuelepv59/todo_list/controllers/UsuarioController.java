package org.educastur.samuelepv59.todo_list.controllers;

import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.dtos.ActualizarUsuarioRequest;
import org.educastur.samuelepv59.todo_list.dtos.UsuarioResponse;
import org.educastur.samuelepv59.todo_list.services.UsuarioService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Gestión de usuarios y perfiles")
public class UsuarioController {

    private final UsuarioService userService;

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<UsuarioResponse> getAll() {
        return userService.getAll();
    }

    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario específico por su identificador")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public UsuarioResponse obtenerPorId(@Parameter(description = "ID del usuario") @PathVariable Long id) {
        return userService.obtenerPorId(id);
    }

    @Operation(summary = "Actualizar perfil de usuario", description = "Modifica los datos del perfil de un usuario existente")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UsuarioResponse actualizar(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @RequestBody ActualizarUsuarioRequest request) {
        return userService.actualizar(id, request);
    }

    @Operation(summary = "Eliminar usuario", description = "Borra un usuario del sistema")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void eliminar(@Parameter(description = "ID del usuario") @PathVariable Long id) {
        userService.eliminar(id);
    }

    @Operation(summary = "Promocionar usuario (ADMIN)", description = "Cambia el rol del usuario a GESTOR")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/promote")
    public UsuarioResponse promote(@Parameter(description = "ID del usuario") @PathVariable Long id) {
        return userService.ascenderAGestor(id);
    }

    @Operation(summary = "Degradar usuario (ADMIN)", description = "Cambia el rol del usuario a USER")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/demote")
    public UsuarioResponse demote(@Parameter(description = "ID del usuario") @PathVariable Long id) {
        return userService.degradarAUsuario(id);
    }
}

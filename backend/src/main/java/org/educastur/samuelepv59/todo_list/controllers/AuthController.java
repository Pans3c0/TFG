package org.educastur.samuelepv59.todo_list.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.dtos.AuthResponse;
import org.educastur.samuelepv59.todo_list.dtos.CreateUserRequest;
import org.educastur.samuelepv59.todo_list.dtos.LoginRequest;
import org.educastur.samuelepv59.todo_list.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para registro e inicio de sesión")
public class AuthController {

    private final UsuarioService userService;

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un usuario en el sistema y devuelve un token JWT")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Credenciales del usuario") @RequestBody LoginRequest request) {
        try {
            AuthResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
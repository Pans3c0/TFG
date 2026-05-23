package org.educastur.samuelepv59.todo_list.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el login. El frontend envía usuario y contraseña,
 * nosotros validamos y devolvemos el token en AuthResponse.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String nombreUsuario;
    private String contrasena;
}
package org.educastur.samuelepv59.todo_list.dtos;

import lombok.*;

/**
 * DTO para el registro de nuevos usuarios.
 * Se valida que las contraseñas coincidan y que el usuario no exista ya.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    private String nombreUsuario;
    private String correo;
    private String nombreCompleto;
    private String contrasena;
    private String verifyPassword;
}
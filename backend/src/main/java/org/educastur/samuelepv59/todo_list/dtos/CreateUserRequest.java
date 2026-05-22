package org.educastur.samuelepv59.todo_list.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO REGISTRO
public class CreateUserRequest {
    private String nombreUsuario;
    private String correo;
    private String nombreCompleto;
    private String contrasena;
    private String verifyPassword;
}
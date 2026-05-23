package org.educastur.samuelepv59.todo_list.dtos;

import lombok.*;

/**
 * DTO para actualizar el perfil del usuario.
 * El frontend puede cambiar email, nombre completo y contraseña.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarUsuarioRequest {
    private String correo;
    private String nombreCompleto;
    private String contrasena;
}

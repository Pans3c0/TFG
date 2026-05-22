package org.educastur.samuelepv59.todo_list.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarUsuarioRequest {
    private String correo;
    private String nombreCompleto;
    private String contrasena;
}

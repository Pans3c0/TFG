package org.educastur.samuelepv59.todo_list.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String nombreUsuario;
    private String contrasena;
}
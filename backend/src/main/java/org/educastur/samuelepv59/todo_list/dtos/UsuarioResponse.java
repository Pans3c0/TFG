package org.educastur.samuelepv59.todo_list.dtos;

import org.educastur.samuelepv59.todo_list.models.Usuario;
import org.educastur.samuelepv59.todo_list.models.UserRole;

/**
 * DTO para enviar datos del usuario al frontend.
 * Usamos record porque es inmutable y el frontend solo lee esta info.
 */
public record UsuarioResponse(
        Long id,
        String nombreUsuario,
        String correo,
        String nombreCompleto,
        UserRole Rol) {
    public static UsuarioResponse of(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombreUsuario(),
                usuario.getCorreo(),
                usuario.getNombreCompleto(),
                usuario.getRol());
    }
}

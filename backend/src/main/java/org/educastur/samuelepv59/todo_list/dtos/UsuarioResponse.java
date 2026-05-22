package org.educastur.samuelepv59.todo_list.dtos;

import org.educastur.samuelepv59.todo_list.models.Usuario;
import org.educastur.samuelepv59.todo_list.models.UserRole;

/**
 * Usamos record porque es una clase de "solo lectura" para el frontend.
 * Java genera automáticamente getters, constructor, equals y hashCode.
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

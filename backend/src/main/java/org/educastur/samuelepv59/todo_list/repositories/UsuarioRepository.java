package org.educastur.samuelepv59.todo_list.repositories;

import org.educastur.samuelepv59.todo_list.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
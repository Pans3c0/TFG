package org.educastur.samuelepv59.todo_list.repositories;

import org.educastur.samuelepv59.todo_list.models.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    Optional<Etiqueta> findByNombre(String text);
}
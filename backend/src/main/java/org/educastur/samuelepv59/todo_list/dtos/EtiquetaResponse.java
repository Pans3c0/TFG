package org.educastur.samuelepv59.todo_list.dtos;

import org.educastur.samuelepv59.todo_list.models.Etiqueta;

public record EtiquetaResponse(Long id, String text) {
    public static EtiquetaResponse of(Etiqueta etiqueta) {
        return new EtiquetaResponse(etiqueta.getId(), etiqueta.getNombre());
    }
}

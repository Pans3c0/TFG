package org.educastur.samuelepv59.todo_list.dtos;

import org.educastur.samuelepv59.todo_list.models.Etiqueta;

/**
 * DTO simple para enviar una etiqueta al frontend.
 * Lo usamos anidado en TareaResponse para no hacer queries extra.
 */
public record EtiquetaResponse(Long id, String text) {
    public static EtiquetaResponse of(Etiqueta etiqueta) {
        return new EtiquetaResponse(etiqueta.getId(), etiqueta.getNombre());
    }
}

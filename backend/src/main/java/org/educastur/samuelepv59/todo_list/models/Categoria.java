package org.educastur.samuelepv59.todo_list.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "categorias") // Evitamos conflictos con nombres de tablas
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column(unique = true): No queremos categorías duplicadas.
     *                nullable = false: Una categoría debe tener nombre
     *                obligatoriamente.
     */
    @Column(unique = true, nullable = false, name = "title")
    private String nombre;

    /**
     * RELACIÓN: Una categoría contiene muchas tareas.
     * mappedBy = "categoria": Indica que el campo "categoria" en la clase Tarea es el
     * dueño de la relación.
     * cascade = CascadeType.PERSIST: Si guardas una categoría con tareas nuevas, se
     * guardan todas.
     * 
     * @ToString.Exclude: IMPORTANTE. Evita que al imprimir la categoría se impriman
     *                    todas sus tareas,
     *                    lo que causaría un bucle infinito (StackOverflow) si Tarea
     *                    también imprime su categoría.
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Tarea> tareas = new ArrayList<>();

    // --- MÉTODOS EQUALS Y HASHCODE (Lógica profesional para Proxies de Hibernate)
    // ---

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        Categoria categoria = (Categoria) o;
        return getId() != null && Objects.equals(getId(), categoria.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
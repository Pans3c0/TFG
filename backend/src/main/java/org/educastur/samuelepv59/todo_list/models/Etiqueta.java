package org.educastur.samuelepv59.todo_list.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "etiquetas")
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    @ManyToMany(mappedBy = "etiquetas")
    @Builder.Default
    @ToString.Exclude
    private Set<Tarea> tareas = new HashSet<>();

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
        Etiqueta etiqueta = (Etiqueta) o;
        return getId() != null && Objects.equals(getId(), etiqueta.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}

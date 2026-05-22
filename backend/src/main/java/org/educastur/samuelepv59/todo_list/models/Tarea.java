package org.educastur.samuelepv59.todo_list.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(nullable = false, name = "title")
    private String titulo;

    @Lob
    @Column(name = "description")
    private String descripcion;

    @Column(name = "completed")
    @Builder.Default
    private boolean completada = false;

    @Column(name = "deadline")
    private LocalDateTime fechaLimite;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskPriority prioridad = TaskPriority.MEDIA;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskStatus estado = TaskStatus.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Usuario autor;

    @ManyToMany
    @JoinTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Builder.Default
    private Set<Etiqueta> etiquetas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categoria categoria;

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

        Tarea tarea = (Tarea) o;

        return getId() != null && Objects.equals(getId(), tarea.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
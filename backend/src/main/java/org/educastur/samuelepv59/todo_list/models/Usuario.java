package org.educastur.samuelepv59.todo_list.models;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidad de Usuario.
 * Implementa UserDetails para usar Spring Security.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_entity")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "username")
    private String nombreUsuario;

    @Column(name = "email")
    private String correo;

    @Column(name = "fullname")
    private String nombreCompleto;

    @Column(name = "password")
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole rol = UserRole.USER;

    @Override
    public String getUsername() {
        return this.nombreUsuario;
    }

    @Override
    public String getPassword() {
        return this.contrasena;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
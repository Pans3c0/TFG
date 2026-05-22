package org.educastur.samuelepv59.todo_list.config;

import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.models.Usuario;
import org.educastur.samuelepv59.todo_list.models.UserRole;
import org.educastur.samuelepv59.todo_list.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        userRepository.findByNombreUsuario("admin").ifPresentOrElse(
                existingAdmin -> {
                    if (existingAdmin.getRol() != UserRole.ADMIN) {
                        existingAdmin.setRol(UserRole.ADMIN);
                        userRepository.save(existingAdmin);
                    }
                },
                () -> {
                    Usuario adminUser = Usuario.builder()
                            .nombreUsuario("admin")
                            .correo("admin@todolist.com")
                            .nombreCompleto("Administrador del Sistema")
                            .contrasena(passwordEncoder.encode("CIFPlaboral2026"))
                            .rol(UserRole.ADMIN)
                            .build();

                    userRepository.save(adminUser);
                });
    }
}

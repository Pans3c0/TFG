package org.educastur.samuelepv59.todo_list.services;

import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.dtos.AuthResponse;
import org.educastur.samuelepv59.todo_list.dtos.CreateUserRequest;
import org.educastur.samuelepv59.todo_list.dtos.LoginRequest;
import org.educastur.samuelepv59.todo_list.dtos.ActualizarUsuarioRequest;
import org.educastur.samuelepv59.todo_list.dtos.UsuarioResponse;
import org.educastur.samuelepv59.todo_list.models.Usuario;
import org.educastur.samuelepv59.todo_list.models.UserRole;
import org.educastur.samuelepv59.todo_list.repositories.UsuarioRepository;
import org.educastur.samuelepv59.todo_list.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

        private final UsuarioRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        /**
         * Registra un nuevo usuario en el sistema.
         */
        public AuthResponse register(CreateUserRequest request) {
                Usuario usuario = Usuario.builder()
                                .nombreUsuario(request.getNombreUsuario())
                                .correo(request.getCorreo())
                                .nombreCompleto(request.getNombreCompleto())
                                .contrasena(passwordEncoder.encode(request.getContrasena()))
                                .rol(UserRole.USER)
                                .build();

                Usuario savedUser = userRepository.save(usuario);
                var jwtToken = jwtService.generateToken(savedUser);
                return AuthResponse.builder()
                                .token(jwtToken)
                                .usuario(UsuarioResponse.of(savedUser))
                                .build();
        }

        public AuthResponse login(LoginRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getNombreUsuario(),
                                                request.getContrasena()));
                var usuario = userRepository.findByNombreUsuario(request.getNombreUsuario())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                var jwtToken = jwtService.generateToken(usuario);
                return AuthResponse.builder()
                                .token(jwtToken)
                                .usuario(UsuarioResponse.of(usuario))
                                .build();
        }

        public List<UsuarioResponse> getAll() {
                return userRepository.findAll().stream()
                                .map(UsuarioResponse::of)
                                .toList();
        }

        public UsuarioResponse obtenerPorId(Long id) {
                Usuario usuario = userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                return UsuarioResponse.of(usuario);
        }

        public UsuarioResponse actualizar(Long id, ActualizarUsuarioRequest request) {
                Usuario usuario = userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                if (request.getCorreo() != null)
                        usuario.setCorreo(request.getCorreo());
                if (request.getNombreCompleto() != null)
                        usuario.setNombreCompleto(request.getNombreCompleto());
                if (request.getContrasena() != null)
                        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));

                return UsuarioResponse.of(userRepository.save(usuario));
        }

        public void eliminar(Long id) {
                if (!userRepository.existsById(id)) {
                        throw new RuntimeException("Usuario no encontrado");
                }
                userRepository.deleteById(id);
        }

        public UsuarioResponse ascenderAGestor(Long id) {
                Usuario usuario = userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                usuario.setRol(UserRole.GESTOR);
                return UsuarioResponse.of(userRepository.save(usuario));
        }

        public UsuarioResponse degradarAUsuario(Long id) {
                Usuario usuario = userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                usuario.setRol(UserRole.USER);
                return UsuarioResponse.of(userRepository.save(usuario));
        }
}
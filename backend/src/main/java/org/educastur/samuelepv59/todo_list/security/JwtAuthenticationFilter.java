package org.educastur.samuelepv59.todo_list.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.repositories.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // PASO 1: Leer la cabecera Authorization
        String authHeader = request.getHeader("Authorization");

        // Sin cabecera o sin prefijo Bearer → dejar pasar sin autenticar
        // Spring Security decidirá si el recurso es público o requiere auth
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // PASO 2: Extraer el token ("Bearer " tiene 7 caracteres)
        String token = authHeader.substring(7);

        // PASO 3: Validar firma y expiración
        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // PASO 4 y 5: Extraer datos del token
        String username = jwtService.extractUsername(token);
        List<String> roles = jwtService.extractRoles(token);

        // PASO 6: Cargar el User (aquí desde BBDD; ver nota sobre stateless)
        var principal = userRepository.findByNombreUsuario(username).orElse(null);
        if (principal == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // PASO 7: Construir las GrantedAuthorities a partir de los roles del token
        var authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        // PASO 8: Crear y registrar el objeto Authentication
        // IMPORTANTE: el tercer parámetro (authorities) es lo que activa
        // la autenticación. Sin él, el usuario no queda autenticado.
        var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}

package org.educastur.samuelepv59.todo_list.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.educastur.samuelepv59.todo_list.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expirationMs;

    // ── MÉTODO 1: Generar token a partir de un User ──────────
    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getNombreUsuario()) // Identificador principal
                .claim("roles", getRoles(usuario)) // Roles para autorización
                .claim("userId", usuario.getId()) // ID para OwnerCheck sin BBDD
                .issuedAt(new Date()) // Fecha de emisión
                .expiration(new Date( // Fecha de expiración
                        System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey()) // Firma HMAC-SHA256
                .compact(); // Serializa a String
    }

    // ── MÉTODO 2: Extraer username del token ─────────────────
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // ── MÉTODO 3: Extraer roles del token ────────────────────
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return parseClaims(token).get("roles", List.class);
    }

    // ── MÉTODO 4: Validar token (firma + expiración) ─────────
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token); // Lanza JwtException si es inválido
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token malformado, firma incorrecta o caducado
        }
    }

    // ── MÉTODOS PRIVADOS DE SOPORTE ──────────────────────────
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        // Requiere mínimo 32 bytes; lanza IllegalArgumentException si es corta
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private List<String> getRoles(Usuario usuario) {
        return usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}
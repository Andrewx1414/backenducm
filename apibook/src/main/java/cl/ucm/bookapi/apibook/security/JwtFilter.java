package cl.ucm.bookapi.apibook.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // <-- ¡Nueva importación!
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List; // <-- Nueva importación!

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            // 🔐 Verificar el token con la clave segura del JwtUtil
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(jwtUtil.getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String role = claims.get("role", String.class); // Extrae el rol (ej. "LECTOR", "ADMIN")

            // --- ¡CAMBIO CRÍTICO AQUÍ! ---
            // 1. Crear una SimpleGrantedAuthority a partir del rol.
            // 2. Asegurarse de que el rol tenga el prefijo "ROLE_" (ej. "ROLE_LECTOR").
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()) // Convertimos a mayúsculas y prefijamos
            );

            // 3. Pasar las autoridades al UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            // En caso de que el token sea inválido, expirado, o no se pueda parsear
            // Spring Security por defecto manejará las excepciones de autenticación aquí,
            // pero podemos loguear o enviar una respuesta específica si fuera necesario.
            // Por ahora, simplemente no se establecerá la autenticación en el contexto,
            // y la cadena de filtros continuará, lo que probablemente resultará en 401/403.
            logger.error("Error al validar o procesar el token JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
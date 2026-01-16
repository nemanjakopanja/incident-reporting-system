package org.unibl.etf.pisio.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtGlobalFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // PUBLIC rute
        if (path.startsWith("/api/auth") || path.equals("/api/incidents") || path.startsWith("/api/storage/presigned") /*|| path.startsWith("/api/storage")*/) {
            return chain.filter(exchange);
        }

        // Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            // JWT VALIDACIJA
            String token = authHeader.substring(7);
            Claims claims = jwtUtil.parseToken(token);

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            // ROLE-BASED AUTORIZACIJA
            if ((path.startsWith("/api/users") || path.startsWith("/api/moderator")) && !role.equals("MODERATOR")) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // (OPCIONO) mogu proslijediti podatke dalje
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(builder -> builder
                            .header("X-User-Username", username)
                            .header("X-User-Role", role)
                    )
                    .build();

            return chain.filter(mutatedExchange);

        } catch (JwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1; // prije routinga
    }
}

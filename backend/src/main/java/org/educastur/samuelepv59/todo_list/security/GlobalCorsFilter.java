// package org.educastur.samuelepv59.todo_list.security;

// import jakarta.servlet.Filter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.core.Ordered;
// import org.springframework.core.annotation.Order;
// import org.springframework.stereotype.Component;

// import java.io.IOException;

// @Component
// @Order(Ordered.HIGHEST_PRECEDENCE) // Esto garantiza que sea lo PRIMERO en
// ejecutarse public class GlobalCorsFilter implements Filter {

// @Override
// public void doFilter(ServletRequest req, ServletResponse res, FilterChain
// chain) throws IOException, ServletException {
// HttpServletResponse response = (HttpServletResponse) res;
// HttpServletRequest request = (HttpServletRequest) req;

// // Forzamos las cabeceras en CADA respuesta, pase lo que pase dentro de
// Spring
// response.setHeader("Access-Control-Allow-Origin",
// "https://todofront-production-d1bf.up.railway.app");
// response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS,
// DELETE, PUT, PATCH");
// response.setHeader("Access-Control-Max-Age", "3600");
// response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With,
// Content-Type, Accept, Authorization");
// response.setHeader("Access-Control-Allow-Credentials", "true");

// // Si es una petición OPTIONS (Preflight), respondemos 200 OK inmediatamente
// y cortamos aquí
// if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
// response.setStatus(HttpServletResponse.SC_OK);
// } else {
// // Si no es OPTIONS, dejamos que la petición continúe hacia tu Controller
// chain.doFilter(req, res);
// }
// }
// }
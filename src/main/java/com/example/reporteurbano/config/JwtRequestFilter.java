package com.example.reporteurbano.config;

import com.example.reporteurbano.model.Usuario;
import com.example.reporteurbano.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //busca pelo header authorization
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String cpf = null;

        //se encontrar um authorization e ele ser do tipo bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Remove o "Bearer "
            cpf = jwtUtil.extractCpf(token);
        }

        if (token != null && cpf != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(token, cpf)) {
                Optional<Usuario> usuario = usuarioService.buscarPorCpf(cpf);

                //Codigo Antigo
               // if (usuario.isPresent()) {
                 //   UsernamePasswordAuthenticationToken authentication =
                   //         new UsernamePasswordAuthenticationToken(usuario, null, null);
                //    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                 //   SecurityContextHolder.getContext().setAuthentication(authentication);
             //   }

                //caso usuário exista começa processo de autenticação
                if (usuario.isPresent()) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(cpf, null, List.of());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}

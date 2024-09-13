package com.residencia.restaurante.security.JWT;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    Claims claims= null;

    private String username=null;

    @Autowired
    private DetallesUsuarioService service;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Ruta actual
        String rutaActual= request.getServletPath();

        if("/auth/login".equals(rutaActual) || "/usuario/agregar".equals(rutaActual) ){
            filterChain.doFilter(request,response);
        }else {
            String autorizacionHeader= request.getHeader("Authorization");
            String token=null;

            if(autorizacionHeader!= null && autorizacionHeader.startsWith("Bearer ")){
                token= autorizacionHeader.substring(7);
                username=jwtUtil.extraerUsername(token);
                claims=jwtUtil.extraerTodosClaims(token);
            }else {
                // Manejar caso de encabezado incorrecto o ausente
                // Puedes lanzar un error, enviar una respuesta de error, etc.

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails= service.loadUserByUsername(username);
                if(jwtUtil.validarToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken upA=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    upA.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(upA);
                }
            }

            filterChain.doFilter(request,response);

        }

    }

    public  boolean esAdministrador(){
        return "admin".equalsIgnoreCase((String)claims.get("role"));
    }

    public boolean esCocinero(){
        return "cocinero".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean esComandero(){
        return "comandero".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean esEncargadoAlmacen(){
        return "encargadoAlmacen".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean esEncargadoUtensilio(){
        return "encargadoUtensilio".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean esCajero(){
        return "cajero".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getUsuarioActual(){
        return username;
    }
}

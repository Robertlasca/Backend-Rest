package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.service.IGeneralesService;
import com.residencia.restaurante.security.JWT.DetallesUsuarioService;
import com.residencia.restaurante.security.JWT.JwtFilter;
import com.residencia.restaurante.security.JWT.JwtUtil;
import com.residencia.restaurante.security.repository.IUsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Logger;

@Service
@Slf4j
public class GeneralesServiceImpl implements IGeneralesService {
    @Autowired
    IUsuarioRepository usuarioRepository;

    //private Logger logger=(Logger) LoggerFactory.getLogger(GeneralesServiceImpl.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    DetallesUsuarioService detallesUsuarioService;

    @Autowired
    JwtFilter jwtFilter;


    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("telefono"),requestMap.get("contrasena"))
            );
            if(auth.isAuthenticated()){
                if(detallesUsuarioService.obtenerDetallesUsuario().getEstado().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+jwtUtil.generarToken(detallesUsuarioService.obtenerDetallesUsuario().getEmail(),detallesUsuarioService.obtenerDetallesUsuario().getRol())+"\"}",HttpStatus.OK);
                }else {
                    return new ResponseEntity<String>("{\"mensaje\":\""+"Espere la aprobación del administrador."+"\"}",HttpStatus.BAD_REQUEST);
                }
            }

        }catch (Exception e){
            log.error("{}",e);
        }

        return new ResponseEntity<String>("{\"mensaje\":\""+"Credenciales incorrectas."+"\"}"
                , HttpStatus.BAD_REQUEST);
    }
}

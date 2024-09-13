package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.security.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface IUsuarioParametrosService {
    ResponseEntity<Usuario> obtenerDatosxId(Integer id);
    ResponseEntity<String> actualizarTelefono(Map<String,String> objetoMap);

    ResponseEntity<String> actualizarEmail(Map<String,String> objetoMap);

    ResponseEntity<String> verificarEmail(String token,String nuevoCorreo);

    ResponseEntity<String> actualizarContraseña(Map<String,String> objetoMap);
}

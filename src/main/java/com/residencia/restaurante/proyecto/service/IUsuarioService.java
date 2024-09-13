package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.security.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface IUsuarioService {
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    ResponseEntity<String> cambiarEstado(Map<String,String> objetoMap);

    ResponseEntity<Usuario> obtenerXid(Integer id);

    ResponseEntity<List<Usuario>> obtenerActivos();

    ResponseEntity<List<Usuario>> obtenerNoActivos();

    ResponseEntity<List<Usuario>> obtenerUsuarios();
}

package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.security.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/usuario")
public interface IUsuarioController {
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true)Map<String,String> objetoMap);

    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true)Map<String,String> objetoMap);

    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true)Map<String,String> objetoMap);

    @GetMapping(path = "/obtenerXid/{id}")
    ResponseEntity<Usuario> obtenerXid(@PathVariable Integer id);

    @GetMapping(path = "/obtenerActivos")
    ResponseEntity<List<Usuario>> obtenerActivos();

    @GetMapping(path = "/obtenerNoActivos")
    ResponseEntity<List<Usuario>> obtenerNoActivos();

    @GetMapping(path = "/obtener")
    ResponseEntity<List<Usuario>> obtenerUsuarios();
}

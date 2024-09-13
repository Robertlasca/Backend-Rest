package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.security.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/parametros")
public interface IUsuarioParametrosController {

    @GetMapping(path = "/datos/{id}")
    ResponseEntity<Usuario> obtenerDatosxId(@PathVariable Integer id);

    @PostMapping(path = "/actualizarTelefono")
    ResponseEntity<String> actualizarTelefono(@RequestBody(required = true)Map<String,String> objetoMap);

    @PostMapping(path = "/actualizarContrasena")
    ResponseEntity<String> actualizarContrasena(@RequestBody(required = true)Map<String,String> objetoMap);

    @PostMapping(path = "/actualizarGmail")
    ResponseEntity<String> actualizarGmail(@RequestBody(required = true)Map<String,String> objetoMap);

    @GetMapping(path = "/verificar")
    ResponseEntity<String> verificarGmail(@RequestParam("token") String token, @RequestParam String nuevoCorreo);


}

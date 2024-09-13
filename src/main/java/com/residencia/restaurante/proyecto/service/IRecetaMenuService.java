package com.residencia.restaurante.proyecto.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IRecetaMenuService {
    ResponseEntity<String> eliminarIngrediente(Map<String, String> objetoMap);

    ResponseEntity<String> editarIngredienteReceta(Map<String, String> objetoMap);

    ResponseEntity<String> agregarIngredienteReceta(Map<String, String> objetoMap);
}

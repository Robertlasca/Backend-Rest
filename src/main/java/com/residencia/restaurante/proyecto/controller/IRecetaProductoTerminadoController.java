package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.entity.Inventario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/productoTerminado/receta")
public interface IRecetaProductoTerminadoController {
    @PostMapping(path = "/eliminar/{id}")
    ResponseEntity<String> eliminarIngrediente(@PathVariable Integer id);

    @PostMapping(path = "/editar")
    ResponseEntity<String> editarIngredienteReceta(@RequestBody(required = true) Map<String,String> objetoMap);

    @PostMapping(path ="/agregar")
    ResponseEntity<String> agregarIngredienteReceta(@RequestBody(required = true) Map<String,String> objetoMap);

    @GetMapping(path = "/obtenerMateriasIdProducto/{id}")
    ResponseEntity<List<Inventario>> obtenerMateriasXCProducto(@PathVariable Integer id);

}

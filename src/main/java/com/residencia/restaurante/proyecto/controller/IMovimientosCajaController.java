package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.entity.MovimientosCaja;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/movimientoCaja")
public interface IMovimientosCajaController {
    @GetMapping(path = "/obtenerTipo/{tipo}")
    ResponseEntity<List<MovimientosCaja>> filtarXtipoMovimiento(@PathVariable String tipo);

    @GetMapping(path = "/obtenerMovimientos")
    ResponseEntity<List<MovimientosCaja>> obtenerMovimientos();

    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true)Map<String,String> objetoMap);

    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true)Map<String,String> objetoMap);

    @PostMapping(path = "/eliminar/{id}")
    ResponseEntity<String> eliminar(@PathVariable Integer id);

    @GetMapping(path = "/obtenerXArqueo/{id}")
    ResponseEntity<List<MovimientosCaja>> filtarXArqueo(@PathVariable Integer id);

    @GetMapping(path = "/obtenerXid/{id}")
    ResponseEntity<MovimientosCaja> obtenerXid(@PathVariable Integer id);




}

package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.CajaDTO;
import com.residencia.restaurante.proyecto.dto.MesaDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.Mesa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/mesa")
public interface IMesasController {
    @GetMapping(path = "/mesasActivas/{id}")
    ResponseEntity<List<MesaDTO>> obtenerMesasActivasPorArea(@PathVariable Integer id);
    @GetMapping(path = "/mesasNoActivas/{id}")
    ResponseEntity<List<Mesa>> obtenerMesasNoActivasPorArea(@PathVariable Integer id);
    @GetMapping(path = "/obtenerMesas/{id}")
    ResponseEntity<List<Mesa>> obtenerMesasPorArea(@PathVariable Integer id);

    @PostMapping(path = "/cambiarEstado/{id}")
    ResponseEntity<String> cambiarEstado(@PathVariable Integer id);
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String,String> objetoMap);
    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String,String> objetoMap);

    @PostMapping(path = "/mover")
    ResponseEntity<String> mover(@RequestBody(required = true) Map<String,String> objetoMap);

    @GetMapping(path = "/obtenerMesa/{id}")
    ResponseEntity<Mesa> obtenerMesaId(@PathVariable Integer id);
}

package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.AreaServicioDTO;
import com.residencia.restaurante.proyecto.dto.CajaDTO;
import com.residencia.restaurante.proyecto.entity.AreaServicio;
import com.residencia.restaurante.proyecto.entity.Caja;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/areaServicio")
public interface IAreaServicioController {
    @GetMapping(path = "/areasActivas")
    ResponseEntity<List<AreaServicioDTO>> obtenerAreasActivas();

    @GetMapping(path = "/areasNoActivas")
    ResponseEntity<List<AreaServicioDTO>> obtenerAreasNoActivas();

    @GetMapping(path = "/obtenerAreas")
    ResponseEntity<List<AreaServicioDTO>> obtenerAreas();

    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String,String> objetoMap);

    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String,String> objetoMap);

    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String,String> objetoMap);

    @GetMapping(path = "/obtenerArea/{id}")
    ResponseEntity<AreaServicio> obtenerAreaId(@PathVariable Integer id);

    @PostMapping(path = "/eliminar")
    ResponseEntity<String> eliminar();
}

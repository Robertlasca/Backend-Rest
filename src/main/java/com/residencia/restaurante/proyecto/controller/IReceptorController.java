package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.ComandaDTO;
import com.residencia.restaurante.proyecto.dto.DatosComandaDTO;
import com.residencia.restaurante.proyecto.dto.DatosOrdenesDTO;
import com.residencia.restaurante.proyecto.dto.DetalleOrdenProductoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/receptor")
public interface IReceptorController {
    @GetMapping(path = "/obtenerComandas/{id}")
    ResponseEntity<List<DatosComandaDTO>> obtenerComandasPorIdCocina(@PathVariable Integer id);

    @GetMapping(path = "/obtenerComandasEnPreparacion/{id}")
    ResponseEntity<List<DetalleOrdenProductoDTO>> obtenerComandasEnPreparacionPorIdCocina(@PathVariable Integer id);

    @GetMapping(path = "/obtenerComandasTerminadas/{id}")
    ResponseEntity<List<DetalleOrdenProductoDTO>> obtenerComandasTerminadasPorIdCocina(@PathVariable Integer id);

    @PostMapping(path = "/cambiarPlatilloPreparacion/{id}")
    ResponseEntity<String> cambiarPlatilloPreparacion(@PathVariable Integer id);

    @PostMapping(path = "/cambiarPlatilloTerminado/{id}")
    ResponseEntity<String> cambiarPlatilloTerminado(@PathVariable Integer id);

    @PostMapping(path = "/cambiarPlatilloCancelado/{id}")
    ResponseEntity<String> cambiarPlatilloCancelado(@PathVariable Integer id);

    @PostMapping(path = "/imprimir")
    ResponseEntity<String> cerrarCuenta();

    @GetMapping(path = "/obtenerComandasXPlatillo/{id}")
    ResponseEntity<List<DatosOrdenesDTO>> obtenerComandasAgrupadasPorCantidad(@PathVariable  Integer id);
}

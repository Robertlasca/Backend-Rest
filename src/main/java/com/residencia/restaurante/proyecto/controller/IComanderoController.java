package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.ComandaDTO;
import com.residencia.restaurante.proyecto.dto.OrdenDTO;
import com.residencia.restaurante.proyecto.dto.ProductoDto;
import com.residencia.restaurante.proyecto.entity.Orden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/comandero")
public interface IComanderoController {
    @PostMapping(path = "/abrirOrden")
    ResponseEntity<Orden> abrirOrden(@RequestBody(required = true) Map<String, String> objetoMap);

    @PostMapping(path = "/asignarPlatillos")
    ResponseEntity<String> asignarPlatillos(@RequestBody(required = true) Map<String, String> objetoMap);

    @PostMapping(path = "/enviarACocina")
    ResponseEntity<String> enviarACocina(@RequestBody(required = true) Map<String, String> objetoMap);

    @PostMapping(path = "/asignarPlatillo")
    ResponseEntity<String> asignarPlatillo(@RequestBody(required = true) Map<String, String> objetoMap);

    @PostMapping(path = "/eliminarPlatillo")
    ResponseEntity<String> eliminarPlatilloDeComanda(@RequestBody(required = true) Map<String, String> objetoMap);

    @GetMapping(path = "/imprimirComanda/{id}")
    ResponseEntity<String> imprimirComanda(@PathVariable Integer id);

    @GetMapping(path = "/obtenerProductos")
    ResponseEntity<List<ProductoDto>> obtenerProductos();

    @GetMapping(path = "/obtenerProducto")
    ResponseEntity<ProductoDto> obtenerProducto(@RequestBody(required = true) Map<String, String> objetoMap);

    @GetMapping(path = "/obtenerOrdenes")
    ResponseEntity<List<Orden>> obtenerOrdenes();

    @GetMapping(path = "/obtenerComanda/{id}")
    ResponseEntity<ComandaDTO> obtenerComandaPorIdOrden(@PathVariable Integer id);

    @GetMapping(path = "/obtenerComandaMesa/{id}")
    ResponseEntity<ComandaDTO> obtenerComandaPorIdOrdenMesa(@PathVariable Integer id);

    @PostMapping(path = "/cerrarCuenta")
    ResponseEntity<String> cerrarCuenta(@RequestBody(required = true) Map<String, String> objetoMap);

    @GetMapping(path = "/validarStock")
    ResponseEntity<String> validarStocks(@RequestBody(required = true)String productos);

    @GetMapping(path = "/obtenerOrdenesActuales")
    ResponseEntity<List<OrdenDTO>> obtenerOrdenesActuales();







}

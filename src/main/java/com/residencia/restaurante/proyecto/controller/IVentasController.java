package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.DetalleVentaDTO;
import com.residencia.restaurante.proyecto.dto.VentasDTO;
import com.residencia.restaurante.proyecto.entity.Cocina_Utensilio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/ventas")
public interface IVentasController {
    @GetMapping(path = "/obtener")
    ResponseEntity<List<VentasDTO>> obtenerTodas();

    @GetMapping(path = "/obtenerDetalleVenta/{id}")
    ResponseEntity<DetalleVentaDTO> obtenerDetalleVenta(@PathVariable Integer id);

    @GetMapping(path = "/obtenerPorCaja/{caja}")
    ResponseEntity<List<VentasDTO>> obtenerPorCaja(@PathVariable String caja);

    @GetMapping(path = "/obtenerPorArea/{areaServicio}")
    ResponseEntity<List<VentasDTO>> obtenerPorArea(@PathVariable String areaServicio);


    @GetMapping(path = "/obtenerPorArea/{cliente}")
    ResponseEntity<List<VentasDTO>> obtenerPorCliente(@PathVariable String cliente);

    @GetMapping(path = "/obtenerPorFecha/{fecha}")
    ResponseEntity<List<VentasDTO>> obtenerPorFecha(@PathVariable String fecha);
}

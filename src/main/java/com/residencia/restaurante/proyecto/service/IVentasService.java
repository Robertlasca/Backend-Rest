package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.DetalleVentaDTO;
import com.residencia.restaurante.proyecto.dto.VentasDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IVentasService {
    ResponseEntity<List<VentasDTO>> obtenerTodas();

    ResponseEntity<DetalleVentaDTO> obtenerDetalleVenta(Integer id);

    ResponseEntity<List<VentasDTO>> obtenerPorCaja(String caja);

    ResponseEntity<List<VentasDTO>> obtenerPorArea(String areaServicio);

    ResponseEntity<List<VentasDTO>> obtenerPorCliente(String cliente);

    ResponseEntity<List<VentasDTO>> obtenerPorFecha(String fecha);
}

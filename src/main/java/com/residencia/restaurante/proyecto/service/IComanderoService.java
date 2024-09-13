package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.ComandaDTO;
import com.residencia.restaurante.proyecto.dto.OrdenDTO;
import com.residencia.restaurante.proyecto.dto.ProductoDto;
import com.residencia.restaurante.proyecto.entity.Orden;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IComanderoService {
    ResponseEntity<Orden> abrirOrden(Map<String, String> objetoMap);

    ResponseEntity<String> asignarPlatillos(Map<String, String> objetoMap);

    ResponseEntity<List<ProductoDto>> obtenerProductos();

    ResponseEntity<List<Orden>> obtenerOrdenes();

    ResponseEntity<ProductoDto> obtenerProducto(Map<String, String> objetoMap);

    ResponseEntity<ComandaDTO> obtenerComandaPorIdOrden(Integer id);

    ResponseEntity<ComandaDTO> obtenerComandaPorIdOrdenMesa(Integer id);

    ResponseEntity<String> cerrarCuenta(Map<String,String> objetoMap);

    ResponseEntity<String> validarStocks(String productos);


    ResponseEntity<List<OrdenDTO>> obtenerOrdenesActuales();

    ResponseEntity<String> asignarPlatillo(Map<String, String> objetoMap);

    ResponseEntity<String> imprimirComanda(Integer id);

    ResponseEntity<String> eliminarPlatilloDeComanda(Map<String, String> objetoMap);

    ResponseEntity<String> enviarACocina(Map<String, String> objetoMap);
}

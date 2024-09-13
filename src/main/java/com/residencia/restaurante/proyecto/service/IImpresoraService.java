package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.ImpresoraDTO;
import com.residencia.restaurante.proyecto.entity.Impresora;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de impresoras en el restaurante.
 */
public interface IImpresoraService {

    /**
     * Obtiene todas las impresoras activas.
     * @return ResponseEntity con la lista de impresoras activas.
     */
    ResponseEntity<List<ImpresoraDTO>> obtenerImpresorasActivas();

    /**
     * Obtiene todas las impresoras no activas.
     * @return ResponseEntity con la lista de impresoras no activas.
     */
    ResponseEntity<List<ImpresoraDTO>> obtenerImpresorasNoActivas();

    /**
     * Obtiene todas las impresoras registradas.
     * @return ResponseEntity con la lista de impresoras.
     */
    ResponseEntity<List<ImpresoraDTO>> obtenerImpresoras();

    /**
     * Cambia el estado de una impresora.
     * @param objetoMap Mapa que contiene la información necesaria para cambiar el estado de la impresora.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> cambiarEstado(Map<String,String> objetoMap);

    /**
     * Agrega una nueva impresora.
     * @param objetoMap Mapa que contiene la información necesaria para agregar la impresora.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    /**
     * Actualiza una impresora existente.
     * @param objetoMap Mapa que contiene la información necesaria para actualizar la impresora.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    /**
     * Obtiene una impresora específica por su ID.
     * @param id El ID de la impresora.
     * @return ResponseEntity con la impresora obtenida.
     */
    ResponseEntity<Impresora> obtenerImpresorasId(Integer id);
}

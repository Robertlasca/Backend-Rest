package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.CajaDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de cajas en el restaurante.
 */
public interface ICajaService {

    /**
     * Obtiene todas las cajas activas.
     * @return ResponseEntity con la lista de cajas activas.
     */
    ResponseEntity<List<CajaDTO>> obtenerCajasActivas();

    /**
     * Obtiene todas las cajas no activas.
     * @return ResponseEntity con la lista de cajas no activas.
     */
    ResponseEntity<List<CajaDTO>> obtenerCajasNoActivas();

    /**
     * Obtiene todas las cajas registradas.
     * @return ResponseEntity con la lista de cajas.
     */
    ResponseEntity<List<CajaDTO>> obtenerCajas();

    /**
     * Cambia el estado de una caja.
     * @param objetoMap Mapa que contiene la información necesaria para cambiar el estado de la caja.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> cambiarEstado(Map<String,String> objetoMap);

    /**
     * Agrega una nueva caja.
     * @param objetoMap Mapa que contiene la información necesaria para agregar la caja.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    /**
     * Actualiza una caja existente.
     * @param objetoMap Mapa que contiene la información necesaria para actualizar la caja.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    /**
     * Obtiene una caja específica por su ID.
     * @param id El ID de la caja.
     * @return ResponseEntity con la caja obtenida.
     */
    ResponseEntity<Caja> obtenerCajaId(Integer id);

    ResponseEntity<List<CajaDTO>> obtenerCajasConArqueo();
}

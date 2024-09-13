package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.EstacionDTO;
import com.residencia.restaurante.proyecto.entity.Cocina;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de cocinas en el restaurante.
 */
public interface ICocinaService {

    /**
     * Obtiene todas las cocinas activas.
     * @return ResponseEntity con la lista de cocinas activas.
     */
    ResponseEntity<List<EstacionDTO>> obtenerCocinasActivas();

    /**
     * Obtiene todas las cocinas no activas.
     * @return ResponseEntity con la lista de cocinas no activas.
     */
    ResponseEntity<List<EstacionDTO>> obtenerCocinasNoActivas();

    /**
     * Obtiene todas las cocinas registradas.
     * @return ResponseEntity con la lista de cocinas.
     */
    ResponseEntity<List<EstacionDTO>> obtenerCocinas();

    /**
     * Cambia el estado de una cocina.
     * @param objetoMap Mapa que contiene la información necesaria para cambiar el estado de la cocina.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> cambiarEstado(Map<String,String> objetoMap);

    /**
     * Agrega una nueva cocina.
     * @param objetoMap Mapa que contiene la información necesaria para agregar la cocina.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    /**
     * Actualiza una cocina existente.
     * @param objetoMap Mapa que contiene la información necesaria para actualizar la cocina.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    /**
     * Obtiene una cocina específica por su ID.
     * @param id El ID de la cocina.
     * @return ResponseEntity con la cocina obtenida.
     */
    ResponseEntity<Cocina> obtenerCocinasId(Integer id);

    ResponseEntity<List<Cocina>> obtenerCocinasActivasSinAlmacen();
}

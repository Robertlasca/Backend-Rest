package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.AlmacenDTO;
import com.residencia.restaurante.proyecto.entity.Almacen;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de almacenes.
 */
public interface IAlmacenService {

    /**
     * Obtiene todos los almacenes activos.
     * @return ResponseEntity con la lista de almacenes activos.
     */
    ResponseEntity<List<AlmacenDTO>> obtenerAlmacenActivos();

    /**
     * Obtiene todos los almacenes no activos.
     * @return ResponseEntity con la lista de almacenes no activos.
     */
    ResponseEntity<List<AlmacenDTO>> obtenerAlmacenNoActivos();

    /**
     * Obtiene todos los almacenes.
     * @return ResponseEntity con la lista de todos los almacenes.
     */
    ResponseEntity<List<AlmacenDTO>> obtenerAlmacen();

    /**
     * Cambia el estado de un almacén.
     * @param objetoMap Mapa con los datos necesarios para cambiar el estado del almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación.
     */
    ResponseEntity<String> cambiarEstado(Map<String,String> objetoMap);

    /**
     * Agrega un nuevo almacén.
     * @param objetoMap Mapa con los datos del nuevo almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación.
     */
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    /**
     * Actualiza un almacén existente.
     * @param objetoMap Mapa con los datos actualizados del almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación.
     */
    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    /**
     * Obtiene un almacén por su ID.
     * @param id El ID del almacén que se desea obtener.
     * @return ResponseEntity con el almacén obtenido.
     */
    ResponseEntity<Almacen> obtenerAlmacenId(Integer id);

}

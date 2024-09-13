package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.MedioPagoDTO;
import com.residencia.restaurante.proyecto.entity.MedioPago;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de medios de pago en el restaurante.
 */
public interface IMedioPagoService {

    /**
     * Obtiene todos los medios de pago activos.
     * @return ResponseEntity con la lista de medios de pago activos.
     */
    ResponseEntity<List<MedioPagoDTO>> obtenerMedioPagoActivas();

    /**
     * Obtiene todos los medios de pago no activos.
     * @return ResponseEntity con la lista de medios de pago no activos.
     */
    ResponseEntity<List<MedioPagoDTO>> obtenerMedioPagoNoActivas();

    /**
     * Obtiene todos los medios de pago.
     * @return ResponseEntity con la lista de todos los medios de pago.
     */
    ResponseEntity<List<MedioPagoDTO>> obtenerMedioPago();

    /**
     * Cambia el estado de un medio de pago.
     * @param objetoMap Mapa que contiene la información necesaria para cambiar el estado.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> cambiarEstado(Map<String,String> objetoMap);

    /**
     * Agrega un nuevo medio de pago.
     * @param objetoMap Mapa que contiene la información necesaria para agregar el medio de pago.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    /**
     * Actualiza un medio de pago existente.
     * @param objetoMap Mapa que contiene la información necesaria para actualizar el medio de pago.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    /**
     * Obtiene un medio de pago por su ID.
     * @param id El ID del medio de pago.
     * @return ResponseEntity con el medio de pago correspondiente al ID.
     */
    ResponseEntity<MedioPago> obtenerMedioPagoId(Integer id);
}

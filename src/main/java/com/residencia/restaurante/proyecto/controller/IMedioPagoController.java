package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.MedioPagoDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.MedioPago;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Interfaz del controlador para la gestión de medios de pago en el restaurante.
 * Proporciona endpoints para obtener información sobre medios de pago activos e inactivos,
 * así como para la creación, actualización y cambio de estado de los medios de pago.
 */
@RequestMapping(path = "/medioPago")
public interface IMedioPagoController {

    /**
     * Obtiene una lista de todos los medios de pago activos en el sistema.
     *
     * @return ResponseEntity con la lista de medios de pago activos y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/medioPagoActivos")
    ResponseEntity<List<MedioPagoDTO>> obtenerMedioPagoActivos();

    /**
     * Obtiene una lista de todos los medios de pago que actualmente no están activos en el sistema.
     *
     * @return ResponseEntity con la lista de medios de pago no activos y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/medioPagoNoActivos")
    ResponseEntity<List<MedioPagoDTO>> obtenerMedioPagoNoActivos();

    /**
     * Obtiene una lista de todos los medios de pago registrados en el sistema, independientemente de su estado.
     *
     * @return ResponseEntity con la lista completa de medios de pago y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerMedioPago")
    ResponseEntity<List<MedioPagoDTO>> obtenerMedioPago();

    /**
     * Cambia el estado de un medio de pago específico en el sistema.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cambiar el estado del medio de pago.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Agrega un nuevo medio de pago al sistema con los datos proporcionados.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para crear un nuevo medio de pago.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Actualiza los datos de un medio de pago existente en el sistema. La información para la actualización
     * se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para actualizar el medio de pago.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Obtiene los detalles de un medio de pago específico por su identificador.
     *
     * @param id El identificador único del medio de pago a consultar.
     * @return ResponseEntity con los detalles del medio de pago solicitado y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerMedioPago/{id}")
    ResponseEntity<MedioPago> obtenerMedioPagoId(@PathVariable Integer id);
}

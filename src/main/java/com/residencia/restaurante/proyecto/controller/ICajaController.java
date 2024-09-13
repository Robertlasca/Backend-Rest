package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.CajaDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * Interfaz del controlador para la gestión de cajas dentro del sistema.
 * Proporciona endpoints para obtener información sobre cajas activas e inactivas,
 * así como para la creación, actualización, y cambio de estado de las cajas.
 */
@RequestMapping(path = "/caja")
public interface ICajaController {
    /**
     * Obtiene una lista de todas las cajas activas en el sistema.
     *
     * @return ResponseEntity con la lista de cajas activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/cajasActivas")
    ResponseEntity<List<CajaDTO>> obtenerCajasActivas();
    /**
     * Obtiene una lista de todas las cajas que actualmente no están activas en el sistema.
     *
     * @return ResponseEntity con la lista de cajas no activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/cajasNoActivas")
    ResponseEntity<List<CajaDTO>> obtenerCajasNoActivas();
    /**
     * Obtiene una lista de todas las cajas registradas en el sistema, independientemente de su estado.
     *
     * @return ResponseEntity con la lista completa de cajas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerCajas")
    ResponseEntity<List<CajaDTO>> obtenerCajas();

    @GetMapping(path = "/obtenerCajasConArqueo")
    ResponseEntity<List<CajaDTO>> obtenerCajasConArqueo();
    /**
     * Cambia el estado de una caja específica en el sistema.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cambiar el estado de la caja(estado y id).
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String,String> objetoMap);
    /**
     * Agrega una nueva caja al sistema con los datos proporcionados.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para crear una nueva caja.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String,String> objetoMap);
    /**
     * Actualiza los datos de una caja existente en el sistema. La información para la actualización
     * se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para actualizar la caja.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String,String> objetoMap);
    /**
     * Obtiene los detalles de una caja específica por su identificador.
     *
     * @param id El identificador único de la caja a consultar.
     * @return ResponseEntity con los detalles de la caja solicitada y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerCaja/{id}")
    ResponseEntity<Caja> obtenerCajaId(@PathVariable Integer id);
}

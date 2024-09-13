package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.ImpresoraDTO;
import com.residencia.restaurante.proyecto.entity.Cocina;
import com.residencia.restaurante.proyecto.entity.Impresora;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Interfaz del controlador para la gestión de impresoras en el restaurante.
 * Proporciona endpoints para obtener información sobre impresoras activas e inactivas,
 * así como para la creación, actualización y cambio de estado de las impresoras.
 */
@RequestMapping(path = "/impresora")
public interface IImpresoraController {

    /**
     * Obtiene una lista de todas las impresoras activas en el sistema.
     *
     * @return ResponseEntity con la lista de impresoras activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/impresoraActivas")
    ResponseEntity<List<ImpresoraDTO>> obtenerImpresoraActivas();

    /**
     * Obtiene una lista de todas las impresoras que actualmente no están activas en el sistema.
     *
     * @return ResponseEntity con la lista de impresoras no activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/impresoraNoActivas")
    ResponseEntity<List<ImpresoraDTO>> obtenerImpresoraNoActivas();

    /**
     * Obtiene una lista de todas las impresoras registradas en el sistema, independientemente de su estado.
     *
     * @return ResponseEntity con la lista completa de impresoras y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerImpresoras")
    ResponseEntity<List<ImpresoraDTO>> obtenerImpresora();

    /**
     * Cambia el estado de una impresora específica en el sistema. La información necesaria
     * para identificar la impresora y el nuevo estado se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cambiar el estado de la impresora.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Agrega una nueva impresora al sistema con los datos proporcionados.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para crear una nueva impresora.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Actualiza los datos de una impresora existente en el sistema. La información para la actualización
     * se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para actualizar la impresora.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Obtiene los detalles de una impresora específica por su identificador.
     *
     * @param id El identificador único de la impresora a consultar.
     * @return ResponseEntity con los detalles de la impresora solicitada y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerImpresora/{id}")
    ResponseEntity<Impresora> obtenerImpresoraId(@PathVariable Integer id);
}

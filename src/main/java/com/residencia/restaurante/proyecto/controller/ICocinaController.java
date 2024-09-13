package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.EstacionDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.Cocina;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Interfaz del controlador para la gestión de estaciones de cocina en el restaurante.
 * Proporciona endpoints para obtener información sobre estaciones de cocina activas e inactivas,
 * así como para la creación, actualización y cambio de estado de las estaciones de cocina.
 */
@RequestMapping(path = "/estacion")
public interface ICocinaController {

    /**
     * Obtiene una lista de todas las estaciones de cocina activas en el sistema.
     *
     * @return ResponseEntity con la lista de estaciones de cocina activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/cocinaActivas")
    ResponseEntity<List<EstacionDTO>> obtenerCocinaActivas();

    /**
     * Obtiene una lista de todas las estaciones de cocina que actualmente no están activas en el sistema.
     *
     * @return ResponseEntity con la lista de estaciones de cocina no activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/cocinaNoActivas")
    ResponseEntity<List<EstacionDTO>> obtenerCocinaNoActivas();

    /**
     * Obtiene una lista de todas las estaciones de cocina registradas en el sistema, independientemente de su estado.
     *
     * @return ResponseEntity con la lista completa de estaciones de cocina y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerCocinas")
    ResponseEntity<List<EstacionDTO>> obtenerCocina();

    /**
     * Cambia el estado de una estación de cocina específica en el sistema. La información necesaria
     * para identificar la estación de cocina y el nuevo estado se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cambiar el estado de la estación de cocina.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Agrega una nueva estación de cocina al sistema con los datos proporcionados.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para crear una nueva estación de cocina.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Actualiza los datos de una estación de cocina existente en el sistema. La información para la actualización
     * se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para actualizar la estación de cocina.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Obtiene los detalles de una estación de cocina específica por su identificador.
     *
     * @param id El identificador único de la estación de cocina a consultar.
     * @return ResponseEntity con los detalles de la estación de cocina solicitada y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerCocina/{id}")
    ResponseEntity<Cocina> obtenerCocinaId(@PathVariable Integer id);

    @GetMapping(path = "/cocinasActivasSinAlmacen")
    ResponseEntity<List<Cocina>> obtenerCocinasActivasSinAlmacen();
}

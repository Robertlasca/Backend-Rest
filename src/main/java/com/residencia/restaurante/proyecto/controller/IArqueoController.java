package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.entity.Arqueo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * Interfaz del controlador para la gestión de arqueos en el sistema.
 * Define endpoints para la apertura y cierre de arqueos, así como para la consulta de arqueos,
 * ya sean todos, activos, o específicos por empleado o ID.
 */
@RequestMapping(path = "/arqueo")
public interface IArqueoController {
    /**
     * Abre un nuevo arqueo con la información proporcionada.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para abrir un nuevo arqueo.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/abrir")
    ResponseEntity<String> abrirArqueo(@RequestBody(required = true)Map<String,String> objetoMap);
    /**
     * Cierra un arqueo existente, identificado por los datos proporcionados.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cerrar un arqueo.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/cerrar")
    ResponseEntity<String> cerrarArqueo(@RequestBody(required = true)Map<String,String> objetoMap);
    /**
     * Obtiene una lista de todos los arqueos registrados en el sistema.
     *
     * @return ResponseEntity con la lista de arqueos y el estado HTTP.
     */
    @GetMapping(path = "/obtenerArqueos")
    ResponseEntity<List<Arqueo>> obtenerArqueos();
    /**
     * Obtiene una lista de todos los arqueos activos en el sistema.
     *
     * @return ResponseEntity con la lista de arqueos activos y el estado HTTP.
     */
    @GetMapping(path = "/obtenerArqueosActivos")
    ResponseEntity<List<Arqueo>> obtenerArqueosActivos();
    /**
     * Obtiene una lista de arqueos específicos de un empleado por su ID.
     *
     * @param id El identificador del empleado para el cual se buscan los arqueos.
     * @return ResponseEntity con la lista de arqueos del empleado y el estado HTTP.
     */
    @GetMapping(path = "/obtenerArqueosEmpleado/{id}")
    ResponseEntity<List<Arqueo>> obtenerArqueosXEmpleado(@PathVariable Integer id);
    /**
     * Obtiene los detalles de un arqueo específico por su ID.
     *
     * @param id El identificador del arqueo a consultar.
     * @return ResponseEntity con los detalles del arqueo solicitado y el estado HTTP.
     */
    @GetMapping(path = "/obtenerArqueo/{id}")
    ResponseEntity<Arqueo> obtenerArqueoId(@PathVariable Integer id);



}

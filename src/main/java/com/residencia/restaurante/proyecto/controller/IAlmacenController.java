package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.AlmacenDTO;
import com.residencia.restaurante.proyecto.entity.Almacen;
import com.residencia.restaurante.proyecto.entity.Cocina;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * Define endpoints para la gestión de almacenes, incluyendo la consulta de almacenes activos e inactivos,
 * el cambio de estados de almacenes, y la adición y actualización de información de almacenes.
 */
@RequestMapping(path = "/almacen")
public interface IAlmacenController {
    /**
     * Obtiene una lista de todos los almacenes activos.
     *
     * @return ResponseEntity con la lista de almacenes activos y el estado HTTP.
     */
    @GetMapping(path = "/almacenesActivos")
    ResponseEntity<List<AlmacenDTO>> obtenerAlmacenActivos();

//PRueba admin
    @GetMapping(path = "/admin")
    ResponseEntity<String> pruebaAdmin();

    /**
     *
     * Obtiene una lista de todos los almacenes no activos.
     *
     * @return ResponseEntity con la lista de almacenes no activos y el estado HTTP.
     */
    @GetMapping(path = "/almacenesNoActivos")
    ResponseEntity<List<AlmacenDTO>> obtenerAlmacenNoActivos();
    /**
     * Obtiene una lista de todos los almacenes, independientemente de su estado.
     *
     * @return ResponseEntity con la lista completa de almacenes y el estado HTTP.
     */
    @GetMapping(path = "/obtenerAlmacenes")
    ResponseEntity<List<AlmacenDTO>> obtenerAlmacen();
    /**
     * Cambia el estado de un almacén específico, activándolo o desactivándolo.
     *
     * @param objetoMap Un mapa que contiene el identificador del almacén y el nuevo estado.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String,String> objetoMap);
    /**
     * Agrega un nuevo almacén con la información proporcionada.
     *
     * @param objetoMap Un mapa que contiene los datos del nuevo almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String,String> objetoMap);
    /**
     * Actualiza la información de un almacén existente.
     *
     * @param objetoMap Un mapa que contiene los datos actualizados del almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String,String> objetoMap);
    /**
     * Obtiene los detalles de un almacén específico por su ID.
     *
     * @param id El identificador del almacén a consultar.
     * @return ResponseEntity con los detalles del almacén solicitado y el estado HTTP.
     */
    @GetMapping(path = "/obtenerAlmacen/{id}")
    ResponseEntity<Almacen> obtenerAlmacenId(@PathVariable Integer id);


}

package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.MovimientoInventarioDTO;
import com.residencia.restaurante.proyecto.entity.Inventario;
import com.residencia.restaurante.proyecto.entity.MateriaPrima;
import com.residencia.restaurante.proyecto.dto.InventarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Interfaz del controlador para la gestión del inventario en el restaurante.
 * Proporciona endpoints para listar productos por stock, listar productos por almacenes,
 * obtener datos de transferencia, agregar productos al inventario, descontar productos del inventario
 * y transferir productos entre almacenes.
 */
@RequestMapping(path = "/inventario")
public interface IInventarioController {

    /**
     * Obtiene una lista de productos del inventario ordenados por stock.
     *
     * @return ResponseEntity con la lista de productos del inventario ordenados por stock
     * y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/listarPorStock")
    ResponseEntity<List<InventarioDTO>> listarPorStock();

    @GetMapping(path = "/obtenerMateriaIdCocina/{id}")
    ResponseEntity<List<Inventario>> obtenerMateriasXCocinaID(@PathVariable Integer id);

    /**
     * Obtiene una lista de materias primas del inventario en un almacén específico.
     *
     * @param objetoMap Un mapa que contiene los id de los almacenes.
     * @return ResponseEntity con la lista de materias primas del inventario en el almacén especificado
     * y el estado HTTP correspondiente.
     */
    @PostMapping(path = "/listarPorAlmacenes")
    ResponseEntity<List<MateriaPrima>> listarPorAlmacenes(@RequestBody(required = true) Map<String,String> objetoMap);

    /**
     * Obtiene una lista de materias primas del inventario en un almacén específico.
     *
     * @param id El ID del almacén.
     * @return ResponseEntity con la lista de materias primas del inventario en el almacén especificado
     * y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/listarPorAlmacen/{id}")
    ResponseEntity<List<MateriaPrima>> listarPorAlmacen(@PathVariable Integer id);

    /**
     * Obtiene datos de transferencia para un producto del inventario.
     *
     * @param objetoMap Un mapa que contiene información relevante para la transferencia.
     * @return ResponseEntity con los datos de transferencia y el estado HTTP correspondiente.
     */
    @PostMapping(path = "/obtenerDatos")
    ResponseEntity<Map<String,String>> obtenerDatostranferencia(@RequestBody(required = true) Map<String,String> objetoMap);

    /**
     * Agrega una materia prima al inventario.
     *
     * @param objetoMap Un mapa que contiene los datos de la materia prima a agregar.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String,String> objetoMap);

    /**
     * Descontar stock de una materia prima del inventario.
     *
     * @param objetoMap Un mapa que contiene los datos de la materia prima a descontar.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/descontar")
    ResponseEntity<String> descontar(@RequestBody(required = true) Map<String,String> objetoMap);

    /**
     * Transferir una materia prima entre almacenes.
     *
     * @param objetoMap Un mapa que contiene los datos de la transferencia.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/transferir")
    ResponseEntity<String> transferir(@RequestBody(required = true) Map<String,String> objetoMap);

    /**
     * Obtiene datos para mostrar de una materia prima del inventario.
     *
     * @param objetoMap Un mapa que contiene información relevante para mostrar.
     * @return ResponseEntity con los datos para mostrar y el estado HTTP correspondiente.
     */
    @PostMapping(path = "/obtenerDatosMostrar")
    ResponseEntity<Map<String,String>> obtenerDatos(@RequestBody(required = true) Map<String,String> objetoMap);

    @GetMapping(path = "/movimientosInventario")
    ResponseEntity<List<MovimientoInventarioDTO>> listarMovimientos();
}

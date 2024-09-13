package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.entity.ProductoNormal;
import com.residencia.restaurante.proyecto.dto.ProductoNormalDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Interfaz del controlador para la gestión de productos normales en el restaurante.
 * Proporciona endpoints para obtener información sobre productos normales activos e inactivos,
 * así como para la creación, actualización y cambio de estado de los productos normales.
 */
@RequestMapping(path = "/productoNormal")
public interface
IProductoNormalController {

    /**
     * Obtiene una lista de todos los productos normales activos en el sistema.
     *
     * @return ResponseEntity con la lista de productos normales activos y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/productoNormalActivos")
    ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesActivos();

    /**
     * Obtiene una lista de todos los productos normales que actualmente no están activos en el sistema.
     *
     * @return ResponseEntity con la lista de productos normales no activos y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/productoNormalNoActivos")
    ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesNoActivos();

    /**
     * Obtiene una lista de todos los productos normales registrados en el sistema, independientemente de su estado.
     *
     * @return ResponseEntity con la lista completa de productos normales y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerProductosNormales")
    ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormales();

    /**
     * Cambia el estado de un producto normal específico en el sistema. La información necesaria
     * para identificar el producto normal y el nuevo estado se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cambiar el estado del producto normal.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Agrega un nuevo producto normal al sistema con los datos proporcionados.
     *
     * @param nombre El nombre del producto.
     * @param descripcion La descripción del producto.
     * @param idCategoria El ID de la categoría del producto.
     * @param stockMax El stock máximo del producto.
     * @param stockMin El stock mínimo del producto.
     * @param stockActual El stock actual del producto.
     * @param costoUnitario El costo unitario del producto.
     * @param margenGanancia El margen de ganancia del producto.
     * @param precioUnitario El precio unitario del producto.
     * @param file La imagen del producto.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestParam("nombre") String nombre,
                                   @RequestParam("descripcion") String descripcion,
                                   @RequestParam("idCategoria") int idCategoria,
                                   @RequestParam("stockMax") int stockMax,
                                   @RequestParam("stockMin") int stockMin,
                                   @RequestParam("stockActual") int stockActual,
                                   @RequestParam("costoUnitario") double costoUnitario,
                                   @RequestParam("margenGanacia") double margenGanancia,
                                   @RequestParam("precioUnitario") double precioUnitario,
                                   @RequestParam(value = "img",required = false) MultipartFile file);

    /**
     * Actualiza los datos de un producto normal existente en el sistema. La información para la actualización
     * se pasa en el cuerpo de la solicitud.
     *
     * @param id El ID del producto normal a actualizar.
     * @param nombre El nuevo nombre del producto.
     * @param descripcion La nueva descripción del producto.
     * @param idCategoria El nuevo ID de la categoría del producto.
     * @param stockMax El nuevo stock máximo del producto.
     * @param stockMin El nuevo stock mínimo del producto.
     * @param stockActual El nuevo stock actual del producto.
     * @param costoUnitario El nuevo costo unitario del producto.
     * @param margenGanancia El nuevo margen de ganancia del producto.
     * @param precioUnitario El nuevo precio unitario del producto.
     * @param file La nueva imagen del producto.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestParam("id") Integer id,
                                      @RequestParam("nombre") String nombre,
                                      @RequestParam("descripcion") String descripcion,
                                      @RequestParam("idCategoria") int idCategoria,
                                      @RequestParam("stockMax") int stockMax,
                                      @RequestParam("stockMin") int stockMin,
                                      @RequestParam("stockActual") int stockActual,
                                      @RequestParam("costoUnitario") double costoUnitario,
                                      @RequestParam("margenGanacia") double margenGanancia,
                                      @RequestParam("precioUnitario") double precioUnitario,
                                      @RequestParam(value = "img",required = false) MultipartFile file);

    /**
     * Obtiene los detalles de un producto normal específico por su identificador.
     *
     * @param id El identificador único del producto normal a consultar.
     * @return ResponseEntity con los detalles del producto normal solicitado y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerProductoNormal/{id}")
    ResponseEntity<ProductoNormal> obtenerProductoNormalId(@PathVariable Integer id);

    /**
     * Lista los productos normales ordenados por stock actual en el sistema.
     *
     * @return ResponseEntity con la lista de productos normales ordenados por stock actual y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/listarPorStock")
    ResponseEntity<List<ProductoNormalDTO>> listarPorStock();

    @GetMapping(path = "/obtenerProductoPorCategoria/{id}")
    ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesPorCategoria(@PathVariable Integer id);

    @GetMapping(path = "/totalProductoNormales")
    ResponseEntity<Integer> obtenerTotalProductosNormales();
}

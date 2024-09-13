package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.entity.ProductoNormal;
import com.residencia.restaurante.proyecto.dto.ProductoNormalDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de productos normales en el restaurante.
 */
public interface IProductoNormalService {

    /**
     * Obtiene todos los productos normales activos.
     * @return ResponseEntity con la lista de productos normales activos.
     */
    ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesActivos();

    /**
     * Obtiene todos los productos normales no activos.
     * @return ResponseEntity con la lista de productos normales no activos.
     */
    ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesNoActivos();

    /**
     * Obtiene todos los productos normales.
     * @return ResponseEntity con la lista de todos los productos normales.
     */
    ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormales();

    /**
     * Cambia el estado de un producto normal.
     * @param objetoMap Mapa que contiene la información necesaria para cambiar el estado.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap);

    /**
     * Agrega un nuevo producto normal.
     * @param nombre Nombre del producto normal.
     * @param descripcion Descripción del producto normal.
     * @param idCategoria ID de la categoría del producto normal.
     * @param stockMax Stock máximo del producto normal.
     * @param stockMin Stock mínimo del producto normal.
     * @param stockActual Stock actual del producto normal.
     * @param costoUnitario Costo unitario del producto normal.
     * @param margenGanancia Margen de ganancia del producto normal.
     * @param precioUnitario Precio unitario del producto normal.
     * @param file Archivo de imagen del producto normal.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> agregar(String nombre,
                                   String descripcion,
                                   int idCategoria,
                                   int stockMax,
                                   int stockMin,
                                   int stockActual,
                                   double costoUnitario,
                                   double margenGanancia,
                                   double precioUnitario,
                                   MultipartFile file);

    /**
     * Actualiza un producto normal existente.
     * @param id ID del producto normal a actualizar.
     * @param nombre Nombre del producto normal.
     * @param descripcion Descripción del producto normal.
     * @param idCategoria ID de la categoría del producto normal.
     * @param stockMax Stock máximo del producto normal.
     * @param stockMin Stock mínimo del producto normal.
     * @param stockActual Stock actual del producto normal.
     * @param costoUnitario Costo unitario del producto normal.
     * @param margenGanancia Margen de ganancia del producto normal.
     * @param precioUnitario Precio unitario del producto normal.
     * @param file Archivo de imagen del producto normal.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> actualizar(Integer id,
                                      String nombre,
                                      String descripcion,
                                      int idCategoria,
                                      int stockMax,
                                      int stockMin,
                                      int stockActual,
                                      double costoUnitario,
                                      double margenGanancia,
                                      double precioUnitario,
                                      MultipartFile file);

    /**
     * Obtiene un producto normal por su ID.
     * @param id El ID del producto normal.
     * @return ResponseEntity con el producto normal correspondiente al ID.
     */
    ResponseEntity<ProductoNormal> obtenerProductoNormalId(Integer id);

    /**
     * Lista los productos normales por su stock.
     * @return ResponseEntity con la lista de productos normales ordenados por su stock.
     */
    ResponseEntity<List<ProductoNormalDTO>> listarPorStock();

    ResponseEntity<Integer> obtenerTotalProductosNormales();

    ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesPorCategoria(Integer id);
}

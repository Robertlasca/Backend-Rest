package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IProductoNormalController;
import com.residencia.restaurante.proyecto.entity.ProductoNormal;
import com.residencia.restaurante.proyecto.service.IProductoNormalService;
import com.residencia.restaurante.proyecto.dto.ProductoNormalDTO;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Implementación del controlador para la gestión de productos normales en el restaurante.
 */
@RestController
public class ProductoNormalControllerImpl implements IProductoNormalController {

    @Autowired
    IProductoNormalService productoNormalService;

    /**
     * Obtiene la lista de productos normales activos.
     *
     * @return ResponseEntity con la lista de productos normales activos y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesActivos() {
        try {
            return productoNormalService.obtenerProductosNormalesActivos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene la lista de productos normales no activos.
     *
     * @return ResponseEntity con la lista de productos normales no activos y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesNoActivos() {
        try {
            return productoNormalService.obtenerProductosNormalesNoActivos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene la lista completa de productos normales.
     *
     * @return ResponseEntity con la lista completa de productos normales y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormales() {
        try {
            return productoNormalService.obtenerProductosNormales();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cambia el estado de un producto normal.
     *
     * @param objetoMap Mapa que contiene los datos necesarios para cambiar el estado del producto normal.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return productoNormalService.cambiarEstado(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega un nuevo producto normal.
     *
     * @param nombre          Nombre del producto normal.
     * @param descripcion     Descripción del producto normal.
     * @param idCategoria     ID de la categoría del producto normal.
     * @param stockMax        Stock máximo del producto normal.
     * @param stockMin        Stock mínimo del producto normal.
     * @param stockActual     Stock actual del producto normal.
     * @param costoUnitario   Costo unitario del producto normal.
     * @param margenGanancia  Margen de ganancia del producto normal.
     * @param precioUnitario  Precio unitario del producto normal.
     * @param file            Archivo asociado al producto normal.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> agregar(String nombre, String descripcion, int idCategoria, int stockMax, int stockMin, int stockActual, double costoUnitario, double margenGanancia, double precioUnitario, MultipartFile file) {
        try {
            return productoNormalService.agregar(nombre, descripcion, idCategoria, stockMax, stockMin, stockActual, costoUnitario, margenGanancia, precioUnitario, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Actualiza un producto normal existente.
     *
     * @param id              ID del producto normal a actualizar.
     * @param nombre          Nuevo nombre del producto normal.
     * @param descripcion     Nueva descripción del producto normal.
     * @param idCategoria     Nuevo ID de la categoría del producto normal.
     * @param stockMax        Nuevo stock máximo del producto normal.
     * @param stockMin        Nuevo stock mínimo del producto normal.
     * @param stockActual     Nuevo stock actual del producto normal.
     * @param costoUnitario   Nuevo costo unitario del producto normal.
     * @param margenGanancia  Nuevo margen de ganancia del producto normal.
     * @param precioUnitario  Nuevo precio unitario del producto normal.
     * @param file            Nuevo archivo asociado al producto normal.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> actualizar(Integer id, String nombre, String descripcion, int idCategoria, int stockMax, int stockMin, int stockActual, double costoUnitario, double margenGanancia, double precioUnitario, MultipartFile file) {
        try {
            return productoNormalService.actualizar(id, nombre, descripcion, idCategoria, stockMax, stockMin, stockActual, costoUnitario, margenGanancia, precioUnitario, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene un producto normal por su ID.
     *
     * @param id El ID único del producto normal.
     * @return ResponseEntity con el producto normal solicitado y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<ProductoNormal> obtenerProductoNormalId(Integer id) {
        try {
            return productoNormalService.obtenerProductoNormalId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductoNormal(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Lista los productos normales por stock.
     *
     * @return ResponseEntity con la lista de productos normales ordenados por stock y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<ProductoNormalDTO>> listarPorStock() {
        try {
            return productoNormalService.listarPorStock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoNormalDTO>> obtenerProductosNormalesPorCategoria(Integer id) {
        try {
            return productoNormalService.obtenerProductosNormalesPorCategoria(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Integer> obtenerTotalProductosNormales() {
        try {
            return productoNormalService.obtenerTotalProductosNormales();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(0,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

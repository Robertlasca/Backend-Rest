package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.CategoriaDTO;
import com.residencia.restaurante.proyecto.entity.Categoria;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de categorías en el restaurante.
 */
public interface ICategoriaService {

    /**
     * Obtiene todas las categorías activas.
     * @return ResponseEntity con la lista de categorías activas.
     */
    ResponseEntity<List<CategoriaDTO>> obtenerCategoriasActivas();

    /**
     * Obtiene todas las categorías no activas.
     * @return ResponseEntity con la lista de categorías no activas.
     */
    ResponseEntity<List<CategoriaDTO>> obtenerCategoriasNoActivas();

    /**
     * Obtiene todas las categorías registradas.
     * @return ResponseEntity con la lista de categorías.
     */
    ResponseEntity<List<CategoriaDTO>> obtenerCategorias();

    /**
     * Cambia el estado de una categoría.
     * @param objetoMap Mapa que contiene la información necesaria para cambiar el estado de la categoría.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> cambiarEstado(Map<String,String> objetoMap);

    /**
     * Agrega una nueva categoría.
     * @param objetoMap Mapa que contiene la información necesaria para agregar la categoría.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    /**
     * Actualiza una categoría existente.
     * @param objetoMap Mapa que contiene la información necesaria para actualizar la categoría.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    /**
     * Obtiene una categoría específica por su ID.
     * @param id El ID de la categoría.
     * @return ResponseEntity con la categoría obtenida.
     */
    ResponseEntity<Categoria> obtenerCategoriasId(Integer id);

    ResponseEntity<List<CategoriaDTO>> obtenerCategoriasMenu();

    ResponseEntity<List<CategoriaDTO>> obtenerCategoriasProductoTerminado();

    ResponseEntity<List<CategoriaDTO>> obtenerCategoriasProductoNormal();
}

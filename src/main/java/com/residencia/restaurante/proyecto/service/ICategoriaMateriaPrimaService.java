package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.CategoriaMateriaPrimaDTO;
import com.residencia.restaurante.proyecto.entity.CategoriaMateriaPrima;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de gestión de categorías de materia prima en el restaurante.
 */
public interface ICategoriaMateriaPrimaService {

    /**
     * Obtiene todas las categorías de materia prima activas.
     * @return ResponseEntity con la lista de categorías activas.
     */
    ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoriasActivas();

    /**
     * Obtiene todas las categorías de materia prima no activas.
     * @return ResponseEntity con la lista de categorías no activas.
     */
    ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoriasNoActivas();

    /**
     * Obtiene todas las categorías de materia prima registradas.
     * @return ResponseEntity con la lista de categorías.
     */
    ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategorias();

    /**
     * Cambia el estado de una categoría de materia prima.
     * @param objetoMap Mapa que contiene la información necesaria para cambiar el estado de la categoría.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> cambiarEstado(Map<String,String> objetoMap);

    /**
     * Agrega una nueva categoría de materia prima.
     * @param objetoMap Mapa que contiene la información necesaria para agregar la categoría.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    /**
     * Actualiza una categoría de materia prima existente.
     * @param objetoMap Mapa que contiene la información necesaria para actualizar la categoría.
     * @return ResponseEntity con el resultado de la operación.
     */
    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    /**
     * Obtiene una categoría de materia prima específica por su ID.
     * @param id El ID de la categoría.
     * @return ResponseEntity con la categoría obtenida.
     */
    ResponseEntity<CategoriaMateriaPrima> obtenerCategoriasId(Integer id);
}

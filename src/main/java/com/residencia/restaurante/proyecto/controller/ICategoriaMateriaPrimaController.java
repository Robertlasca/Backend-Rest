package com.residencia.restaurante.proyecto.controller;

import com.residencia.restaurante.proyecto.dto.CategoriaMateriaPrimaDTO;
import com.residencia.restaurante.proyecto.entity.Categoria;
import com.residencia.restaurante.proyecto.entity.CategoriaMateriaPrima;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Interfaz del controlador para la gestión de categorías de materia prima.
 * Proporciona endpoints para obtener información sobre categorías activas e inactivas,
 * así como para la creación, actualización y cambio de estado de las categorías de materia prima.
 */
@RequestMapping(path = "/categoriaMateriaPrima")
public interface ICategoriaMateriaPrimaController {

    /**
     * Obtiene una lista de todas las categorías de materia prima activas en el sistema.
     *
     * @return ResponseEntity con la lista de categorías activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/categoriasActivas")
    ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoriaActivas();

    /**
     * Obtiene una lista de todas las categorías de materia prima que actualmente no están activas en el sistema.
     *
     * @return ResponseEntity con la lista de categorías no activas y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/categoriasNoActivas")
    ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoriaNoActivas();

    /**
     * Obtiene una lista de todas las categorías de materia prima registradas en el sistema, independientemente de su estado.
     *
     * @return ResponseEntity con la lista completa de categorías y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerCategorias")
    ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoria();

    /**
     * Cambia el estado de una categoría de materia prima específica en el sistema. La información necesaria
     * para identificar la categoría y el nuevo estado se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cambiar el estado de la categoría.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/cambiarEstado")
    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Agrega una nueva categoría de materia prima al sistema con los datos proporcionados.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para crear una nueva categoría.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/agregar")
    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Actualiza los datos de una categoría de materia prima existente en el sistema. La información para la actualización
     * se pasa en el cuerpo de la solicitud.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para actualizar la categoría.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @PostMapping(path = "/actualizar")
    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String, String> objetoMap);

    /**
     * Obtiene los detalles de una categoría de materia prima específica por su identificador.
     *
     * @param id El identificador único de la categoría a consultar.
     * @return ResponseEntity con los detalles de la categoría solicitada y el estado HTTP correspondiente.
     */
    @GetMapping(path = "/obtenerCategoria/{id}")
    ResponseEntity<CategoriaMateriaPrima> obtenerCategoriaId(@PathVariable Integer id);
}
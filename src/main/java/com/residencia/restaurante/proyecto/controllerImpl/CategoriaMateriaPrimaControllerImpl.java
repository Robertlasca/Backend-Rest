package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.ICategoriaMateriaPrimaController;
import com.residencia.restaurante.proyecto.dto.CategoriaMateriaPrimaDTO;
import com.residencia.restaurante.proyecto.entity.Categoria;
import com.residencia.restaurante.proyecto.entity.CategoriaMateriaPrima;
import com.residencia.restaurante.proyecto.service.ICategoriaMateriaPrimaService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementación del controlador para la gestión de categorías de materia prima en el restaurante.
 */
@RestController
public class CategoriaMateriaPrimaControllerImpl implements ICategoriaMateriaPrimaController {

    @Autowired
    ICategoriaMateriaPrimaService categoriaMateriaPrimaService;

    /**
     * Obtiene una lista de categorías activas de materia prima.
     *
     * @return ResponseEntity con la lista de categorías activas de materia prima y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoriaActivas() {
        try {
            return categoriaMateriaPrimaService.obtenerCategoriasActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de categorías no activas de materia prima.
     *
     * @return ResponseEntity con la lista de categorías no activas de materia prima y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoriaNoActivas() {
        try {
            return categoriaMateriaPrimaService.obtenerCategoriasNoActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todas las categorías de materia prima.
     *
     * @return ResponseEntity con la lista de todas las categorías de materia prima y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaMateriaPrimaDTO>> obtenerCategoria() {
        try {
            return categoriaMateriaPrimaService.obtenerCategorias();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cambia el estado de una categoría de materia prima.
     *
     * @param objetoMap Mapa que contiene el identificador de la categoría y el nuevo estado.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return categoriaMateriaPrimaService.cambiarEstado(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega una nueva categoría de materia prima.
     *
     * @param objetoMap Mapa que contiene los datos de la nueva categoría de materia prima.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return categoriaMateriaPrimaService.agregar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Actualiza una categoría de materia prima existente.
     *
     * @param objetoMap Mapa que contiene los datos actualizados de la categoría de materia prima.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return categoriaMateriaPrimaService.actualizar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una categoría de materia prima por su identificador único.
     *
     * @param id El identificador único de la categoría de materia prima.
     * @return ResponseEntity con la categoría de materia prima solicitada y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<CategoriaMateriaPrima> obtenerCategoriaId(Integer id) {
        try {
            return categoriaMateriaPrimaService.obtenerCategoriasId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new CategoriaMateriaPrima(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

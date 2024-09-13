package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.ICategoriaController;
import com.residencia.restaurante.proyecto.dto.CategoriaDTO;
import com.residencia.restaurante.proyecto.entity.Categoria;
import com.residencia.restaurante.proyecto.service.ICategoriaService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementación del controlador para la gestión de categorías en el restaurante.
 */
@RestController
public class CategoriaControllerImpl implements ICategoriaController {

    @Autowired
    ICategoriaService categoriaService;

    /**
     * Obtiene una lista de categorías activas.
     *
     * @return ResponseEntity con la lista de categorías activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriaActivas() {
        try {
            return categoriaService.obtenerCategoriasActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriaMenu() {
        try {
            return categoriaService.obtenerCategoriasMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriaProductoTerminado() {
        try {
            return categoriaService.obtenerCategoriasProductoTerminado();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriaNormal() {
        try {
            return categoriaService.obtenerCategoriasProductoNormal();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de categorías no activas.
     *
     * @return ResponseEntity con la lista de categorías no activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriaNoActivas() {
        try {
            return categoriaService.obtenerCategoriasNoActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todas las categorías.
     *
     * @return ResponseEntity con la lista de todas las categorías y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoria() {
        try {
            return categoriaService.obtenerCategorias();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cambia el estado de una categoría.
     *
     * @param objetoMap Mapa que contiene el identificador de la categoría y el nuevo estado.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return categoriaService.cambiarEstado(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega una nueva categoría.
     *
     * @param objetoMap Mapa que contiene los datos de la nueva categoría.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return categoriaService.agregar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param objetoMap Mapa que contiene los datos actualizados de la categoría.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return categoriaService.actualizar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una categoría por su identificador único.
     *
     * @param id El identificador único de la categoría.
     * @return ResponseEntity con la categoría solicitada y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Categoria> obtenerCategoriaId(Integer id) {
        try {
            return categoriaService.obtenerCategoriasId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Categoria(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.ICocinaController;
import com.residencia.restaurante.proyecto.dto.EstacionDTO;
import com.residencia.restaurante.proyecto.entity.Cocina;
import com.residencia.restaurante.proyecto.entity.MedioPago;
import com.residencia.restaurante.proyecto.service.ICocinaService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementación del controlador para la gestión de cocinas en el restaurante.
 */
@RestController
public class CocinaControllerImpl implements ICocinaController {

    @Autowired
    ICocinaService cocinaService;

    /**
     * Obtiene una lista de cocinas activas.
     *
     * @return ResponseEntity con la lista de cocinas activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<EstacionDTO>> obtenerCocinaActivas() {
        try {
            return cocinaService.obtenerCocinasActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de cocinas no activas.
     *
     * @return ResponseEntity con la lista de cocinas no activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<EstacionDTO>> obtenerCocinaNoActivas() {
        try {
            return cocinaService.obtenerCocinasNoActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todas las cocinas.
     *
     * @return ResponseEntity con la lista de todas las cocinas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<EstacionDTO>> obtenerCocina() {
        try {
            return cocinaService.obtenerCocinas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cambia el estado de una cocina.
     *
     * @param objetoMap Mapa que contiene el identificador de la cocina y el nuevo estado.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return cocinaService.cambiarEstado(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega una nueva cocina.
     *
     * @param objetoMap Mapa que contiene los datos de la nueva cocina.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return cocinaService.agregar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Actualiza una cocina existente.
     *
     * @param objetoMap Mapa que contiene los datos actualizados de la cocina.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return cocinaService.actualizar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una cocina por su identificador único.
     *
     * @param id El identificador único de la cocina.
     * @return ResponseEntity con la cocina solicitada y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Cocina> obtenerCocinaId(Integer id) {
        try {
            return cocinaService.obtenerCocinasId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Cocina(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Cocina>> obtenerCocinasActivasSinAlmacen() {
        try {
            return cocinaService.obtenerCocinasActivasSinAlmacen();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

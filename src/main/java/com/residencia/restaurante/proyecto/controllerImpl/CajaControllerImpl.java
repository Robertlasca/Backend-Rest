package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.ICajaController;
import com.residencia.restaurante.proyecto.dto.CajaDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.service.ICajaService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementación del controlador para la gestión de cajas en el restaurante.
 */
@RestController
public class CajaControllerImpl implements ICajaController {

    @Autowired
    private ICajaService cajaService;

    /**
     * Obtiene una lista de cajas activas.
     *
     * @return ResponseEntity con la lista de cajas activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CajaDTO>> obtenerCajasActivas() {
        try {
            return cajaService.obtenerCajasActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de cajas no activas.
     *
     * @return ResponseEntity con la lista de cajas no activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CajaDTO>> obtenerCajasNoActivas() {
        try {
            return cajaService.obtenerCajasNoActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todas las cajas.
     *
     * @return ResponseEntity con la lista de todas las cajas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<CajaDTO>> obtenerCajas() {
        try {
            return cajaService.obtenerCajas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CajaDTO>> obtenerCajasConArqueo() {
        try {
            return cajaService.obtenerCajasConArqueo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cambia el estado de una caja.
     *
     * @param objetoMap Mapa que contiene el identificador de la caja y el nuevo estado.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return cajaService.cambiarEstado(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega una nueva caja.
     *
     * @param objetoMap Mapa que contiene los datos de la nueva caja.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return cajaService.agregar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Actualiza una caja existente.
     *
     * @param objetoMap Mapa que contiene los datos actualizados de la caja.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return cajaService.actualizar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una caja por su identificador único.
     *
     * @param id El identificador único de la caja.
     * @return ResponseEntity con la caja solicitada y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Caja> obtenerCajaId(Integer id) {
        try {
            return cajaService.obtenerCajaId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Caja(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IImpresoraController;
import com.residencia.restaurante.proyecto.dto.ImpresoraDTO;
import com.residencia.restaurante.proyecto.entity.Cocina;
import com.residencia.restaurante.proyecto.entity.Impresora;
import com.residencia.restaurante.proyecto.service.IImpresoraService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Implementación del controlador para la gestión de impresoras en el restaurante.
 */
@RestController
public class ImpresoraControllerImpl implements IImpresoraController {

    @Autowired
    IImpresoraService iImpresoraService;

    /**
     * Obtiene una lista de impresoras activas.
     *
     * @return ResponseEntity con la lista de impresoras activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<ImpresoraDTO>> obtenerImpresoraActivas() {
        try {
            return iImpresoraService.obtenerImpresorasActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de impresoras no activas.
     *
     * @return ResponseEntity con la lista de impresoras no activas y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<ImpresoraDTO>> obtenerImpresoraNoActivas() {
        try {
            return iImpresoraService.obtenerImpresorasNoActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todas las impresoras.
     *
     * @return ResponseEntity con la lista de todas las impresoras y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<ImpresoraDTO>> obtenerImpresora() {
        try {
            return iImpresoraService.obtenerImpresoras();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cambia el estado de una impresora.
     *
     * @param objetoMap Mapa que contiene el identificador de la impresora y el nuevo estado.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return iImpresoraService.cambiarEstado(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega una nueva impresora.
     *
     * @param objetoMap Mapa que contiene los datos de la nueva impresora.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return iImpresoraService.agregar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Actualiza una impresora existente.
     *
     * @param objetoMap Mapa que contiene los datos actualizados de la impresora.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return iImpresoraService.actualizar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una impresora por su identificador único.
     *
     * @param id El identificador único de la impresora.
     * @return ResponseEntity con la impresora solicitada y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Impresora> obtenerImpresoraId(Integer id) {
        try {
            return iImpresoraService.obtenerImpresorasId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Impresora(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

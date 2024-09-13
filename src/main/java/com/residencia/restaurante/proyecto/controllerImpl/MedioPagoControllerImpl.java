package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IMedioPagoController;
import com.residencia.restaurante.proyecto.dto.MedioPagoDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.MedioPago;
import com.residencia.restaurante.proyecto.service.IMedioPagoService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Implementación del controlador para la gestión de medios de pago en el restaurante.
 */
@RestController
public class MedioPagoControllerImpl implements IMedioPagoController {

    @Autowired
    IMedioPagoService medioPagoService;

    /**
     * Obtiene la lista de medios de pago activos.
     *
     * @return ResponseEntity con la lista de medios de pago activos y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<MedioPagoDTO>> obtenerMedioPagoActivos() {
        try {
            return medioPagoService.obtenerMedioPagoActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene la lista de medios de pago no activos.
     *
     * @return ResponseEntity con la lista de medios de pago no activos y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<MedioPagoDTO>> obtenerMedioPagoNoActivos() {
        try {
            return medioPagoService.obtenerMedioPagoNoActivas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene la lista completa de medios de pago.
     *
     * @return ResponseEntity con la lista completa de medios de pago y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<MedioPagoDTO>> obtenerMedioPago() {
        try {
            return medioPagoService.obtenerMedioPago();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cambia el estado de un medio de pago.
     *
     * @param objetoMap Mapa que contiene los datos necesarios para cambiar el estado del medio de pago.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return medioPagoService.cambiarEstado(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega un nuevo medio de pago.
     *
     * @param objetoMap Mapa que contiene los datos del nuevo medio de pago.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return medioPagoService.agregar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Actualiza un medio de pago existente.
     *
     * @param objetoMap Mapa que contiene los datos actualizados del medio de pago.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return medioPagoService.actualizar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene un medio de pago por su ID.
     *
     * @param id El ID único del medio de pago.
     * @return ResponseEntity con el medio de pago solicitado y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<MedioPago> obtenerMedioPagoId(Integer id) {
        try {
            return medioPagoService.obtenerMedioPagoId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new MedioPago(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

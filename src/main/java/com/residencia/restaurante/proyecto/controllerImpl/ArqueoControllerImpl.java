package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IArqueoController;
import com.residencia.restaurante.proyecto.entity.Arqueo;
import com.residencia.restaurante.proyecto.service.IArqueoService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Implementación del controlador para la gestión de arqueos en el restaurante.
 */
@RestController
public class ArqueoControllerImpl implements IArqueoController {

    @Autowired
    private IArqueoService arqueoService;

    /**
     * Abre un nuevo arqueo en el sistema con los datos proporcionados.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para abrir el arqueo.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @Override
    public ResponseEntity<String> abrirArqueo(Map<String, String> objetoMap) {
        try {
            return arqueoService.abrirArqueo(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cierra un arqueo específico en el sistema.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cerrar el arqueo.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @Override
    public ResponseEntity<String> cerrarArqueo(Map<String, String> objetoMap) {
        try {
            return arqueoService.cerrarArqueo(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todos los arqueos registrados en el sistema.
     *
     * @return ResponseEntity con la lista completa de arqueos y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<Arqueo>> obtenerArqueos() {
        try {
            return arqueoService.obtenerArqueos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todos los arqueos activos en el sistema.
     *
     * @return ResponseEntity con la lista de arqueos activos y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<Arqueo>> obtenerArqueosActivos() {
        try {
            return arqueoService.obtenerArqueosActivos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todos los arqueos asociados a un empleado específico.
     *
     * @param id El identificador único del empleado.
     * @return ResponseEntity con la lista de arqueos asociados al empleado y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<Arqueo>> obtenerArqueosXEmpleado(Integer id) {
        try {
            return arqueoService.obtenerArqueoXEmpleado(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene los detalles de un arqueo específico por su identificador.
     *
     * @param id El identificador único del arqueo a consultar.
     * @return ResponseEntity con los detalles del arqueo solicitado y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Arqueo> obtenerArqueoId(Integer id) {
        try {
            return arqueoService.obtenerArqueoId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Arqueo(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

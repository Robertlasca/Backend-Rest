package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IAlmacenController;
import com.residencia.restaurante.proyecto.dto.AlmacenDTO;
import com.residencia.restaurante.proyecto.entity.Almacen;
import com.residencia.restaurante.proyecto.service.IAlmacenService;
import com.residencia.restaurante.security.JWT.JwtFilter;
import com.residencia.restaurante.security.JWT.JwtUtil;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Implementación del controlador para la gestión de almacenes en el restaurante.
 */
@RestController
public class AlmacenControllerImpl implements IAlmacenController {

    @Autowired
    IAlmacenService almacenService;

    @Autowired
    JwtFilter jfilter;

    /**
     * Obtiene una lista de todos los almacenes activos en el sistema.
     *
     * @return ResponseEntity con la lista de almacenes activos y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<AlmacenDTO>> obtenerAlmacenActivos() {
        try {
            return almacenService.obtenerAlmacenActivos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> pruebaAdmin() {
        try {
            if(jfilter.esAdministrador()){
                System.out.println("Si entre por que soy admin");
                return Utils.getResponseEntity("Si entre por que admin",HttpStatus.OK);
            }
            return Utils.getResponseEntity(Constantes.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);


        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todos los almacenes que actualmente no están activos en el sistema.
     *
     * @return ResponseEntity con la lista de almacenes no activos y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<AlmacenDTO>> obtenerAlmacenNoActivos() {
        try {
            return almacenService.obtenerAlmacenNoActivos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene una lista de todos los almacenes registrados en el sistema, independientemente de su estado.
     *
     * @return ResponseEntity con la lista completa de almacenes y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<AlmacenDTO>> obtenerAlmacen() {
        try {
            return almacenService.obtenerAlmacen();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cambia el estado de un almacén específico en el sistema.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para cambiar el estado del almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return almacenService.cambiarEstado(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega un nuevo almacén al sistema con los datos proporcionados.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para agregar un nuevo almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return almacenService.agregar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Actualiza los datos de un almacén existente en el sistema.
     *
     * @param objetoMap Un mapa que contiene los datos necesarios para actualizar el almacén.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación y el estado HTTP.
     */
    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return almacenService.actualizar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene los detalles de un almacén específico por su identificador.
     *
     * @param id El identificador único del almacén a consultar.
     * @return ResponseEntity con los detalles del almacén solicitado y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Almacen> obtenerAlmacenId(Integer id) {
        try {
            return almacenService.obtenerAlmacenId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Almacen(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IInventarioController;
import com.residencia.restaurante.proyecto.dto.MovimientoInventarioDTO;
import com.residencia.restaurante.proyecto.entity.Inventario;
import com.residencia.restaurante.proyecto.entity.MateriaPrima;
import com.residencia.restaurante.proyecto.service.IInventarioService;
import com.residencia.restaurante.proyecto.dto.InventarioDTO;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Implementación del controlador para la gestión del inventario en el restaurante.
 */
@RestController
public class InventarioControllerImpl implements IInventarioController {

    @Autowired
    IInventarioService inventarioService;

    /**
     * Lista los elementos del inventario ordenados por stock.
     *
     * @return ResponseEntity con la lista de elementos del inventario ordenados por stock y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<InventarioDTO>> listarPorStock() {
        try {
            return inventarioService.listarPorStock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Inventario>> obtenerMateriasXCocinaID(Integer id) {
        try {
            return inventarioService.obtenerMateriasXCocinaID(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Lista los elementos del inventario por almacenes específicos.
     *
     * @param objetoMap Mapa que contiene los identificadores de los almacenes.
     * @return ResponseEntity con la lista de elementos del inventario por almacenes y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<MateriaPrima>> listarPorAlmacenes(Map<String, String> objetoMap) {
        try {
            return inventarioService.listarPorAlmacenes(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Lista los elementos del inventario por un almacén específico.
     *
     * @param id El identificador único del almacén.
     * @return ResponseEntity con la lista de elementos del inventario por el almacén especificado y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<MateriaPrima>> listarPorAlmacen(Integer id) {
        try {
            return inventarioService.listarPorAlmacen(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene los datos para una transferencia de inventario.
     *
     * @param objetoMap Mapa que contiene los datos necesarios para la transferencia.
     * @return ResponseEntity con los datos para la transferencia y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Map<String, String>> obtenerDatostranferencia(Map<String, String> objetoMap) {
        try {
            return inventarioService.obtenerDatosTransferencia(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Agrega un nuevo elemento al inventario.
     *
     * @param objetoMap Mapa que contiene los datos del nuevo elemento.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return inventarioService.agregar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Realiza un descuento en el inventario para un elemento específico.
     *
     * @param objetoMap Mapa que contiene los datos para realizar el descuento.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> descontar(Map<String, String> objetoMap) {
        try {
            return inventarioService.descontar(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Transfiere un elemento del inventario de un almacén a otro.
     *
     * @param objetoMap Mapa que contiene los datos para realizar la transferencia.
     * @return ResponseEntity con el resultado de la operación y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<String> transferir(Map<String, String> objetoMap) {
        try {
            return inventarioService.transferir(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Obtiene los datos del inventario para un elemento específico.
     *
     * @param objetoMap Mapa que contiene los datos necesarios para obtener los datos del inventario.
     * @return ResponseEntity con los datos del inventario para el elemento especificado y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<Map<String, String>> obtenerDatos(Map<String, String> objetoMap) {
        try {
            return inventarioService.obtenerDatos(objetoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MovimientoInventarioDTO>> listarMovimientos() {
        try {
            return inventarioService.listarMovimientos();

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

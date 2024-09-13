package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IMovimientosCajaController;
import com.residencia.restaurante.proyecto.entity.MovimientosCaja;
import com.residencia.restaurante.proyecto.service.IMovimientoCajaService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
public class MovimientoCajaControllerImpl implements IMovimientosCajaController {
    @Autowired
    private IMovimientoCajaService movimientoCajaService;
    @Override
    public ResponseEntity<List<MovimientosCaja>> filtarXtipoMovimiento(String tipo) {
        try {
            return movimientoCajaService.filtarXtipoMovimiento(tipo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MovimientosCaja>> obtenerMovimientos() {
        try {
            return movimientoCajaService.obtenerMovimientos();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return movimientoCajaService.agregar(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return movimientoCajaService.actualizar(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> eliminar(Integer id) {
        try {
            return movimientoCajaService.eliminar(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<MovimientosCaja>> filtarXArqueo(Integer id) {
        try {
            return movimientoCajaService.filtarXArqueo(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<MovimientosCaja> obtenerXid(Integer id) {
        try {
            return movimientoCajaService.obtenerXid(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new MovimientosCaja(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

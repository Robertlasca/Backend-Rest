package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IReceptorController;
import com.residencia.restaurante.proyecto.dto.ComandaDTO;
import com.residencia.restaurante.proyecto.dto.DatosComandaDTO;
import com.residencia.restaurante.proyecto.dto.DatosOrdenesDTO;
import com.residencia.restaurante.proyecto.dto.DetalleOrdenProductoDTO;
import com.residencia.restaurante.proyecto.service.IReceptorService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReceptorControllerImpl implements IReceptorController {
    @Autowired
    private IReceptorService receptorService;
    @Override
    public ResponseEntity<List<DatosComandaDTO>> obtenerComandasPorIdCocina(Integer id) {
        try {

            return receptorService.obtenerComandasPorIdCocina(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<DatosComandaDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<DetalleOrdenProductoDTO>> obtenerComandasEnPreparacionPorIdCocina(Integer id) {
        try {

            return receptorService.obtenerComandasEnPreparacionPorIdCocina(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<DetalleOrdenProductoDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<DetalleOrdenProductoDTO>> obtenerComandasTerminadasPorIdCocina(Integer id) {
        try {

            return receptorService.obtenerComandasTerminadasPorIdCocina(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<DetalleOrdenProductoDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cambiarPlatilloPreparacion(Integer id) {
        try{
            return receptorService.cambiarPlatilloPreparacion(id);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cambiarPlatilloTerminado(Integer id) {
        try{
            return receptorService.cambiarPlatilloTerminado(id);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cambiarPlatilloCancelado(Integer id) {
        try{
            return receptorService.cambiarPlatilloCancelado(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cerrarCuenta() {
        try {
            return receptorService.cerrarCuenta();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DatosOrdenesDTO>> obtenerComandasAgrupadasPorCantidad(Integer id) {
        try {
            return receptorService.obtenerComandasAgrupadasPorCantidad(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

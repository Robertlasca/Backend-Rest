package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IMesasController;
import com.residencia.restaurante.proyecto.dto.MesaDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.Mesa;
import com.residencia.restaurante.proyecto.service.IMesaService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
public class MesaControllerImpl implements IMesasController {
    @Autowired
    private IMesaService mesaService;
    @Override
    public ResponseEntity<List<MesaDTO>> obtenerMesasActivasPorArea(Integer id) {
        try {
            return mesaService.obtenerMesasActivasPorArea(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Mesa>> obtenerMesasNoActivasPorArea(Integer id) {
        try {
            return mesaService.obtenerMesasNoActivasPorArea(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Mesa>> obtenerMesasPorArea(Integer id) {
        try {
            return mesaService.obtenerMesasPorArea(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cambiarEstado(Integer id) {
        try {
            return mesaService.cambiarEstado(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return mesaService.agregar(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        try {
            return mesaService.actualizar(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> mover(Map<String, String> objetoMap) {
        try {
            return mesaService.mover(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Mesa> obtenerMesaId(Integer id) {
        try {
            return mesaService.obtenerMesaId(id);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<Mesa>(new Mesa(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

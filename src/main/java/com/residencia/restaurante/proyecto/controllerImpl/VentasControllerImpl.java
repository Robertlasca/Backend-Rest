package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.controller.IVentasController;
import com.residencia.restaurante.proyecto.dto.DetalleVentaDTO;
import com.residencia.restaurante.proyecto.dto.VentasDTO;
import com.residencia.restaurante.proyecto.service.IVentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class VentasControllerImpl implements IVentasController {
    @Autowired
    private IVentasService ventasService;

    @Override
    public ResponseEntity<List<VentasDTO>> obtenerTodas() {
        try {
            return ventasService.obtenerTodas();

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<DetalleVentaDTO> obtenerDetalleVenta(Integer id) {
        try {
            return ventasService.obtenerDetalleVenta(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new DetalleVentaDTO(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<VentasDTO>> obtenerPorCaja(String caja) {
        try {
            return ventasService.obtenerPorCaja(caja);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<VentasDTO>> obtenerPorArea(String areaServicio) {
        try {
            return ventasService.obtenerPorArea(areaServicio);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<VentasDTO>> obtenerPorCliente(String cliente) {
        try {
            return ventasService.obtenerPorCliente(cliente);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<VentasDTO>> obtenerPorFecha(String fecha) {
        try {
            return ventasService.obtenerPorFecha(fecha);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IGeneralesController;
import com.residencia.restaurante.proyecto.service.IGeneralesService;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class GeneralesControllerImpl implements IGeneralesController {
    @Autowired
    IGeneralesService generalesService;
    @Override
    public ResponseEntity<String> login(Map<String, String> objetoMap) {
        try {
            return generalesService.login(objetoMap);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

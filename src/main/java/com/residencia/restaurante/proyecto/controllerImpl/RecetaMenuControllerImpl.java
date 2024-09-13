package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IRecetaMenuController;
import com.residencia.restaurante.proyecto.entity.MateriaPrima_Menu;
import com.residencia.restaurante.proyecto.repository.*;
import com.residencia.restaurante.proyecto.service.IRecetaMenuService;
import com.residencia.restaurante.proyecto.serviceImpl.MenuServiceImpl;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class RecetaMenuControllerImpl implements IRecetaMenuController {


    @Autowired
            private IRecetaMenuService menuService;


    @Override
    public ResponseEntity<String> eliminarIngrediente(Map<String, String> objetoMap) {
        try {

            return menuService.eliminarIngrediente(objetoMap);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editarIngredienteReceta(Map<String, String> objetoMap) {
        try {

            return menuService.editarIngredienteReceta(objetoMap);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregarIngredienteReceta(Map<String, String> objetoMap) {
        try {

            return menuService.agregarIngredienteReceta(objetoMap);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

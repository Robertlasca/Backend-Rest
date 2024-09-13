package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IRecetaProductoTerminadoController;
import com.residencia.restaurante.proyecto.entity.Inventario;
import com.residencia.restaurante.proyecto.service.IRecetaProductoTerminadoService;
import com.residencia.restaurante.security.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
public class RecetaProductoTerminadoControllerImpl implements IRecetaProductoTerminadoController {
    @Autowired
    private IRecetaProductoTerminadoService productoTerminadoService;
    @Override
    public ResponseEntity<String> eliminarIngrediente(Integer id) {
        try {
            return productoTerminadoService.eliminarIngrediente(id);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editarIngredienteReceta(Map<String, String> objetoMap) {
        try {
            return productoTerminadoService.editarIngredienteReceta(objetoMap);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> agregarIngredienteReceta(Map<String, String> objetoMap) {
        try {
            return productoTerminadoService.agregarIngredienteReceta(objetoMap);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Inventario>> obtenerMateriasXCProducto(Integer id) {
        try {
            return productoTerminadoService.obtenerMateriasXCProducto(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Inventario>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

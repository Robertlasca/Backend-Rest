package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IUsuarioController;
import com.residencia.restaurante.proyecto.service.IUsuarioService;
import com.residencia.restaurante.security.model.Usuario;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
public class UsuarioControllerImpl implements IUsuarioController {
    @Autowired
    private IUsuarioService usuarioService;
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {
            return usuarioService.agregar(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizar(Map<String, String> objetoMap) {
        return null;
    }

    @Override
    public ResponseEntity<String> cambiarEstado(Map<String, String> objetoMap) {
        try {
            return usuarioService.cambiarEstado(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Usuario> obtenerXid(Integer id) {
        try {
            return usuarioService.obtenerXid(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Usuario(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Usuario>> obtenerActivos() {
        try {
            return usuarioService.obtenerActivos();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Usuario>> obtenerNoActivos() {
        try {
            return usuarioService.obtenerNoActivos();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        try {
            return usuarioService.obtenerUsuarios();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.controller.IUsuarioParametrosController;
import com.residencia.restaurante.proyecto.service.IUsuarioParametrosService;
import com.residencia.restaurante.security.model.Usuario;
import com.residencia.restaurante.security.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UsuarioParametrosControllerImpl implements IUsuarioParametrosController {
    @Autowired
    private IUsuarioParametrosService usuarioParametrosService;

    @Override
    public ResponseEntity<Usuario> obtenerDatosxId(Integer id) {
        try{
            return usuarioParametrosService.obtenerDatosxId(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Usuario(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizarTelefono(Map<String, String> objetoMap) {
        return null;
    }

    @Override
    public ResponseEntity<String> actualizarContrasena(Map<String, String> objetoMap) {
        try {
            return usuarioParametrosService.actualizarContraseña(objetoMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> actualizarGmail(Map<String, String> objetoMap) {
       try {
           return usuarioParametrosService.actualizarEmail(objetoMap);
       }catch (Exception e){
           e.printStackTrace();
       }
       return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> verificarGmail(String token,String nuevoCorreo) {
        try {
            return usuarioParametrosService.verificarEmail(token,nuevoCorreo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

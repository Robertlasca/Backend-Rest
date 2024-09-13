package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.service.IUsuarioService;
import com.residencia.restaurante.security.model.Usuario;
import com.residencia.restaurante.security.repository.IUsuarioRepository;
import com.residencia.restaurante.security.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    @Autowired
    private IUsuarioRepository usuarioRepository;

    PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    @Override
    public ResponseEntity<String> agregar(Map<String, String> objetoMap) {
        try {

            if(objetoMap.containsKey("apellidos") && objetoMap.containsKey("email") && objetoMap.containsKey("contrasena") && objetoMap.containsKey("nombre") && objetoMap.containsKey("telefono")){
                if(!usuarioRepository.existsUsuarioByEmailEqualsIgnoreCase(objetoMap.get("email"))){
                    Usuario usuario=new Usuario();
                    usuario.setNombre(objetoMap.get("nombre"));
                    usuario.setApellidos(objetoMap.get("apellidos"));
                    //usuario.setContrasena(objetoMap.get("contrasena"));
                    usuario.setContrasena(passwordEncoder.encode(objetoMap.get("contrasena")));
                    usuario.setRol(objetoMap.get("rol"));
                    usuario.setEstado("true");
                    usuario.setEmail(objetoMap.get("email"));
                    usuario.setTelefono(objetoMap.get("telefono"));

                    // Generar un token de verificación
                    String verificationToken = UUID.randomUUID().toString();
                    System.out.println(verificationToken.length());
                    usuario.setTokenVerificacionEmail(verificationToken);



                    usuarioRepository.save(usuario);


                    return Utils.getResponseEntity("Usuario registrado con exito.",HttpStatus.OK);
                }
                return Utils.getResponseEntity("Ya esta registrado este correo.",HttpStatus.BAD_REQUEST);
            }

            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

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
            if(objetoMap.containsKey("id") && objetoMap.containsKey("estado")){
                Optional<Usuario> usuarioOptional=usuarioRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(usuarioOptional.isPresent()){
                    Usuario usuario=usuarioOptional.get();

                    if(objetoMap.get("estado").equalsIgnoreCase("false")){
                        usuario.setVisibilidad(false);
                    }else {
                        usuario.setVisibilidad(true);
                    }
                    usuarioRepository.save(usuario);

                    return Utils.getResponseEntity("El estado del usuario ha sido actualizado.",HttpStatus.OK);
                }
                return Utils.getResponseEntity("El usuario no existe.",HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Usuario> obtenerXid(Integer id) {
        try {
            Optional<Usuario> usuarioOptional=usuarioRepository.findById(id);
            if(usuarioOptional.isPresent()){
                return new ResponseEntity<Usuario>(usuarioOptional.get(),HttpStatus.OK);
            }
            return new ResponseEntity<Usuario>(new Usuario(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Usuario>(new Usuario(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Usuario>> obtenerActivos() {
        try {
            return new ResponseEntity<List<Usuario>>(usuarioRepository.getAllByVisibilidadTrue(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Usuario>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Usuario>> obtenerNoActivos() {
        try {
            return new ResponseEntity<List<Usuario>>(usuarioRepository.getAllByVisibilidadFalse(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Usuario>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        try {
            return new ResponseEntity<List<Usuario>>(usuarioRepository.findAll(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Usuario>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

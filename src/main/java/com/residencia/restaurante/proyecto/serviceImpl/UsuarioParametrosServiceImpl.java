package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.constantes.Constantes;
import com.residencia.restaurante.proyecto.service.IUsuarioParametrosService;
import com.residencia.restaurante.security.model.Usuario;
import com.residencia.restaurante.security.repository.IUsuarioRepository;
import com.residencia.restaurante.security.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioParametrosServiceImpl implements IUsuarioParametrosService {
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
            private JavaMailSender mailSender;

    PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    @Override
    public ResponseEntity<Usuario> obtenerDatosxId(Integer id) {
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
    public ResponseEntity<String> actualizarTelefono(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("id") && objetoMap.containsKey("telefono")) {
                Integer id = Integer.parseInt(objetoMap.get("id"));
                Optional<Usuario> optional = usuarioRepository.findById(id);
                if (optional.isPresent()) {
                    Usuario usuario = optional.get();
                    usuario.setTelefono(objetoMap.get("telefono"));
                    return Utils.getResponseEntity("Teléfono actualizado.",HttpStatus.OK);
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
    public ResponseEntity<String> actualizarEmail(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("id") && objetoMap.containsKey("nuevoCorreo")){
                Optional<Usuario> usuarioOptional= usuarioRepository.findById(Integer.parseInt(objetoMap.get("id")));
                if(usuarioOptional.isPresent()){
                    Usuario usuario=usuarioOptional.get();
                    String token=usuario.getTokenVerificacionEmail();
                    String to=objetoMap.get("nuevoCorreo");
                    String verificacionUrl= "http://localhost:8082/parametros/verificar?token=" + token+ "&nuevoCorreo=" + to;
                    SimpleMailMessage message=new SimpleMailMessage();

                    message.setTo(to);
                    message.setSubject("Verificación de correo");
                    message.setText("Para verificar tu correo, por favor haz clicl en el siguiente enlace: "+verificacionUrl);

                    mailSender.send(message);
                    return Utils.getResponseEntity("Por favor revisa tu correo para cambiarlo.",HttpStatus.OK);
                }

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> verificarEmail(String token,String nuevoCorreo) {
        try {
            boolean verificado= verificarUsuario(token,nuevoCorreo);
            if(verificado){
                return Utils.getResponseEntity("Correo verificicado con éxito.",HttpStatus.OK);

            }else {
                return Utils.getResponseEntity("Fallo la verificación del correo.",HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public  boolean verificarUsuario(String token,String nuevoCorreo){
        Optional<Usuario> optionalUsuario= usuarioRepository.findUsuarioByTokenVerificacionEmail(token);

        if(optionalUsuario.isPresent()){
            Usuario usuario= optionalUsuario.get();
            usuario.setVerificacionEmail(true);
            usuario.setEmail(nuevoCorreo);
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<String> actualizarContraseña(Map<String, String> objetoMap) {
        try {
            if(objetoMap.containsKey("id") && objetoMap.containsKey("actualContrasena") && objetoMap.containsKey("nuevaContrasena")){
                Integer id=Integer.parseInt(objetoMap.get("id"));
                Optional<Usuario> optional=usuarioRepository.findById(id);
                if (optional.isPresent()){
                    Usuario usuario= optional.get();
                    if(passwordEncoder.matches(objetoMap.get("actualContrasena"),usuario.getContrasena())){
                        usuario.setContrasena(passwordEncoder.encode(objetoMap.get("nuevaContrasena")));
                        usuarioRepository.save(usuario);
                        return Utils.getResponseEntity("Contaseña actualizada.",HttpStatus.OK);
                    }
                    return Utils.getResponseEntity("Tu contraseña actual es incorrecta.",HttpStatus.BAD_REQUEST);

                }
                return Utils.getResponseEntity("El usuario no existe.",HttpStatus.BAD_REQUEST);

            }
            return Utils.getResponseEntity(Constantes.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return Utils.getResponseEntity(Constantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

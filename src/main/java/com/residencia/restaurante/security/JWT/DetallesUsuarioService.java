package com.residencia.restaurante.security.JWT;

import com.residencia.restaurante.security.model.Usuario;
import com.residencia.restaurante.security.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class DetallesUsuarioService implements UserDetailsService {
    @Autowired
    private IUsuarioRepository usuarioRepository;

    private Usuario usuario;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        usuario=usuarioRepository.findUsuarioByEmail(username);

        if(!Objects.isNull(usuario)){
            return new User(usuario.getEmail(),usuario.getContrasena(),new ArrayList<>());
        }else {
            throw new UsernameNotFoundException("Usuario no encontrado.");
        }
    }

    public Usuario obtenerDetallesUsuario(){
        return usuario;
    }

    
}

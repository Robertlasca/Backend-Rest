package com.residencia.restaurante.security.repository;

import com.residencia.restaurante.security.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario,Integer> {

    boolean existsUsuarioByEmailEqualsIgnoreCase(String email);

    Usuario findUsuarioByEmail(String email);

    List<Usuario> getAllByVisibilidadTrue();

    List<Usuario> getAllByVisibilidadFalse();

    Optional<Usuario> findUsuarioByTokenVerificacionEmail(String token);
}

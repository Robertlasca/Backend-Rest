package com.residencia.restaurante.security.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="usuario")
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 30)
    private String nombre;
    @Column(length = 30)
    private String apellidos;
    @Column(length = 50)
    private String email;
    @Column(length = 100)
    private String contrasena="12345678";
    @Column(length = 12)
    private String telefono;

    @Column(nullable = true)
    private boolean visibilidad=true;
    @Column(nullable = true)
    private boolean verificacionEmail=false;
    @Column(nullable = true,length = 100)
    private String tokenVerificacionEmail;

    private String estado="true";

    private String rol;
}
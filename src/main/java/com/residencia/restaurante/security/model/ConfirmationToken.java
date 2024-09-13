package com.residencia.restaurante.security.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="confirmationToken")
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String token;

    private LocalDateTime creacion;

    private LocalDateTime expiracion;

    @OneToOne
    @JoinColumn(nullable = false,name = "usuario_id")
    private Usuario usuario;

}
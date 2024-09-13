package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa una impresora utilizada en el restaurante.
 * Cada impresora tiene un identificador único, un nombre,
 * una dirección IP, un indicador de si es la impresora por defecto
 * y un estado que indica si está activa o no.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="impresora")
@Entity
public class Impresora {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30)
    private String nombre;

    private String direccionIp;

    private boolean porDefecto;

    private boolean estado=true;
}

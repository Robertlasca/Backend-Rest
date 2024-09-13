package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa una cocina en el restaurante, donde se preparan los platillos.
 * Cada cocina tiene un identificador único, un nombre descriptivo, una descripción opcional y puede estar marcada como visible o no en el sistema.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="cocina")
@Entity
public class Cocina {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30,nullable = true)
    private String nombre;
    @Column(length = 200,nullable = true)
    private String descripcion;
    @Column(nullable = true)
    private boolean visibilidad=true;
    @ManyToOne
    @JoinColumn(nullable = true,name = "impresora_id")
    private Impresora impresora;
}

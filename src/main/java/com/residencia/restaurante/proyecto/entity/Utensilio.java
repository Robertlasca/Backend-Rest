package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Esta clase representa un utensilio utilizado en la cocina del restaurante.
 * Cada utensilio tiene un identificador único, un nombre, una descripción,
 * una cantidad y una referencia a la cocina a la que pertenece.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="utensilio")
@Entity
public class Utensilio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 30)
    private String nombre;
    @Column(length = 50)
    private String descripcion;

    private String imagen;
}

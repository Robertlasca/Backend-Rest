package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa una categoría para clasificar elementos en el sistema del restaurante.
 * Cada categoría tiene un identificador único, un nombre descriptivo y puede estar marcada como visible o no en el sistema.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="categoria")
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30,nullable = true)
    private String nombre;
    @Column(nullable = true)
    private boolean visibilidad=true;
    @Column(nullable = true)
    private String pertenece;
}

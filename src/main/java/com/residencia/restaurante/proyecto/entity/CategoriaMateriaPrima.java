package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa una categoría de materia prima para clasificar los distintos tipos de materias primas.
 * Cada categoría tiene un identificador único, un nombre descriptivo y puede estar marcada como visible o no en el sistema.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="categoriaMateriaPrima")
@Entity
public class CategoriaMateriaPrima {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30,nullable = true)
    private String nombre;
    @Column(nullable = true)
    private boolean visibilidad=true;
}

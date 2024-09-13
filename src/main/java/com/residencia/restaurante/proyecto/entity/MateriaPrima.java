package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Representa una materia prima utilizada en el restaurante.
 * Cada materia prima tiene un identificador único, un nombre,
 * visibilidad, unidad de medida, costo unitario y pertenece a una categoría.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="materiaPrima")
@Entity
public class MateriaPrima {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30)
    private String nombre;

    private boolean visibilidad=true;

    private String unidadMedida;

    private double costoUnitario;

    private String imagen;

    @ManyToOne
    @JoinColumn(nullable = true,name = "categoria_id")
    private CategoriaMateriaPrima categoriaMateriaPrima;


}

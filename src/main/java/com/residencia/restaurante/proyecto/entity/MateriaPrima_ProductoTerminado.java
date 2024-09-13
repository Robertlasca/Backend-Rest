package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa la relación entre una materia prima y un producto terminado,
 * especificando la cantidad de materia prima utilizada en la elaboración
 * del producto terminado.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="materiaPrima_productoTerminado")
@Entity
public class MateriaPrima_ProductoTerminado {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private double cantidad;

    @ManyToOne
    @JoinColumn(name = "productoTerminado_id",nullable = true)
    private ProductoTerminado productoTerminado;


    @ManyToOne
    @JoinColumn(name = "inventario_id",nullable = true)
    private Inventario inventario;

}

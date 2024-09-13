package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa la relación entre una materia prima y un menú,
 * donde se especifica la cantidad de la materia prima necesaria
 * para la elaboración del menú.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="materiaPrima_Menu")
@Entity
public class MateriaPrima_Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "menu_id",nullable = true)
    private Menu menu;

    private double cantidad;

    @ManyToOne
    @JoinColumn(name = "inventario_id",nullable = true)
    private Inventario inventario;
}

package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="productoTerminado_Menu")
@Entity
public class ProductoTerminado_Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private double cantidad;

    @ManyToOne
    @JoinColumn(name = "productoTerminado_id",nullable = true)
    private ProductoTerminado productoTerminado;

    @ManyToOne
    @JoinColumn(name = "menu_id",nullable = true)
    private Menu menu;
}

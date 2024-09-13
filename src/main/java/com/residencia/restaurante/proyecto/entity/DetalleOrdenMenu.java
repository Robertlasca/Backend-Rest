package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa un detalle de una orden que incluye un menú.
 * Cada detalle tiene un identificador único, una referencia a la orden a la que pertenece,
 * una referencia al menú asociado, una cantidad, un comentario opcional,
 * un estado y un total.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="detalleOrden_Menu")
@Entity
public class DetalleOrdenMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = true,name = "orden_id")
    private Orden orden;

    @ManyToOne
    @JoinColumn(nullable = true,name = "menu_id")
    private Menu menu;

    private String nombreMenu;
    private double precioMenu=0;

    private int cantidad;

    private String comentario="Sin comentarios.";

    private String estado;

    private double total;

}

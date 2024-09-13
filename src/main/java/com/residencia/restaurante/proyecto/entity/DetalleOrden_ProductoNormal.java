package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa un detalle de una orden que incluye un producto normal.
 * Cada detalle tiene un identificador único, una referencia a la orden a la que pertenece,
 * una referencia al producto normal asociado, una cantidad, un comentario opcional,
 * un estado y un total.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="detalleOrden_productoNormal")
@Entity
public class DetalleOrden_ProductoNormal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = true,name = "orden_id")
    private Orden orden;

    @ManyToOne
    @JoinColumn(nullable = true,name = "productoNormal_id")
    private ProductoNormal productoNormal;

    private String nombreProductoNormal;
    private double precioProductoNormal=0;

    private int cantidad;

    private String comentario="Sin comentario";

    private String estado;

    private double total;
}

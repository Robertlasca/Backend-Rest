package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Esta clase representa un producto normal en el restaurante.
 * Cada producto normal tiene un identificador único, un nombre, una descripción,
 * un límite máximo de stock, un límite mínimo de stock, el stock actual,
 * un indicador de visibilidad, costo unitario, margen de ganancia, precio unitario,
 * una imagen asociada y una referencia a la categoría a la que pertenece el producto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="productoNormal")
@Entity
public class ProductoNormal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30)
    private String nombre;
    @Column(length = 50)
    private String descripcion;

    private int stockMax;

    private int stockMin;

    private int stockActual;

    private boolean visibilidad=true;

    private double costoUnitario;

    private double margenGanacia;

    private double precioUnitario;

    private String imagen;

    @ManyToOne
    @JoinColumn(nullable = true,name = "categoria_id")
    private Categoria categoria;
}

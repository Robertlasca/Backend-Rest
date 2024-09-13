package com.residencia.restaurante.proyecto.entity;

import com.residencia.restaurante.security.model.Usuario;
import jakarta.persistence.*;
import lombok.*;
/**
 * Esta clase representa un producto terminado en el restaurante.
 * Cada producto terminado tiene un identificador único, un nombre,
 * una unidad de medida, una descripción, el stock actual, el límite máximo de stock,
 * el límite mínimo de stock, un indicador de visibilidad y una referencia a la categoría
 * a la que pertenece el producto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="productoTerminado")
@Entity
public class ProductoTerminado {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30)
    private String nombre;

    private String unidadMedida;
    @Column(length = 50)
    private String descripcion;
    private double stockActual;
    private double stockMax;
    private double stockMin;
    private boolean visibilidad=true;
    private String imagen;

    @ManyToOne
    @JoinColumn(name = "categoria_id",nullable = true)
    private Categoria categoria;
}

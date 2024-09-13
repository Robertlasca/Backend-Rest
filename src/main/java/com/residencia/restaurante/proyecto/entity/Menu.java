package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Esta clase representa un menú en el sistema del restaurante.
 * Cada menú tiene un identificador único, un nombre descriptivo,
 * una descripción opcional, un indicador de dependencia,
 * costos de producción directos, margen de ganancia, precio de venta,
 * visibilidad y está asociado a una cocina, una categoría y una impresora.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="menu")
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30)
    private String nombre;
    @Column(length = 50)
    private String descripcion;

    private boolean isDependent;

    private double costoProduccionDirecto;

    private double margenGanancia;

    private double precioVenta;

    private  boolean visibilidad=true;

    private String imagen;

    @ManyToOne
    @JoinColumn(nullable = true,name = "cocina_id")
    private Cocina cocina;

    @ManyToOne
    @JoinColumn(name = "categoria_id",nullable = true)
    private Categoria categoria;


}

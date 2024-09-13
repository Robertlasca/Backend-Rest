package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Esta clase representa una mesa en el restaurante.
 * Cada mesa tiene un identificador único, un nombre descriptivo,
 * un indicador de tipo, coordenadas (coordX y coordY) para la ubicación física,
 * un estado actual, y visibilidad para determinar si está disponible para los clientes.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="mesa")
@Entity
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30)
    private String nombre;

    private boolean tipo;

    private String tipoMesa;

    private double coordX;

    private double coordY;
    private String estado;
    private boolean visibilidad=true;
    @ManyToOne
    @JoinColumn(nullable = true,name = "areaServicio_id")
    private  AreaServicio areaServicio;


}

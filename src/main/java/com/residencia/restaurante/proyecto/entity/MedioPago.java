package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Esta clase representa un medio de pago utilizado en el sistema del restaurante.
 * Cada medio de pago tiene un identificador único, un nombre descriptivo,
 * una descripción opcional y un estado que indica si está disponible para su uso.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="medioPago")
@Entity
public class MedioPago {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30)
    private String nombre;
    @Column(length = 50)
    private String descripcion;

    private boolean disponibilidad=true;
}

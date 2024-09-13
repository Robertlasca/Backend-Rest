package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Esta clase representa la relación entre una venta y el medio de pago utilizado para esa venta,
 * junto con el monto recibido como pago.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="venta_MedioPago")
@Entity
public class Venta_MedioPago {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = true,name = "medioPago_id")
    private MedioPago medioPago;


    @ManyToOne
    @JoinColumn(nullable = true,name = "venta_id")
    private Venta venta;

    private double pagoRecibido;
}

package com.residencia.restaurante.proyecto.entity;

import com.residencia.restaurante.security.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
/**
 * Esta clase representa una transacción de venta realizada en el restaurante.
 * Cada venta tiene un identificador único, un subtotal, un descuento aplicado,
 * el total a pagar, un comentario opcional, la fecha y hora de consolidación,
 * el estado de la venta, y referencias a la orden, el arqueo y el usuario asociados.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="venta")
@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private double subTotal;

    private double descuento;

    private  double totalPagar;

    private String comentario;

    private LocalDateTime fechaHoraConsolidacion;

    private String estado;

    @ManyToOne
    @JoinColumn(nullable = true,name = "orden_id")
    private Orden orden;

    @ManyToOne
    @JoinColumn(nullable = true,name = "arqueo_id")
    private Arqueo arqueo;

    @ManyToOne
    @JoinColumn(nullable = true,name = "usuario_id")
    private Usuario usuario;


}

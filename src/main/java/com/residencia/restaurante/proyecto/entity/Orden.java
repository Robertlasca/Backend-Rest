package com.residencia.restaurante.proyecto.entity;

import com.residencia.restaurante.security.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/**
 * Esta clase representa una orden realizada por un cliente en el restaurante.
 * Cada orden tiene un identificador único, una referencia a la mesa en la que se realizó la orden,
 * una referencia al usuario que tomó la orden, una referencia a la impresora asociada a la orden,
 * el nombre del cliente que realizó la orden, la cantidad de comensales en la mesa,
 * la fecha y hora de apertura de la orden, y el estado actual de la orden.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="orden")
@Entity
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = true,name = "mesa_id")
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(nullable = true,name = "usuario_id")
    private Usuario usuario;

    private String nombreCliente;

    private int cantidadComensal;

    private LocalDateTime fechaHoraApertura=LocalDateTime.now();

    private String estado;

    private int folio;

    @ManyToOne
    @JoinColumn(nullable = true,name = "caja_id")
    private Caja caja;
}

package com.residencia.restaurante.proyecto.entity;

import com.residencia.restaurante.security.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/**
 * Esta clase representa los movimientos realizados en la caja del restaurante.
 * Cada movimiento tiene un identificador único, un tipo de movimiento (por ejemplo, ingreso, egreso),
 * una cantidad involucrada, un motivo para el movimiento, la fecha y hora del movimiento,
 * una referencia al arqueo de caja asociado y una referencia al usuario que realizó el movimiento.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="movimientoCaja")
@Entity
public class MovimientosCaja {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String tipoMovimiento;

    private double cantidad;

    private String motivo;

    private LocalDateTime fechaHoraMovimiento;

    @ManyToOne
    @JoinColumn(nullable = true,name = "arqueo_id")
    private Arqueo arqueo;

    @ManyToOne
    @JoinColumn(nullable = true,name = "usuario_id")
    private Usuario usuario;
}

package com.residencia.restaurante.proyecto.entity;

import com.residencia.restaurante.security.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
/**
 * Esta clase representa los movimientos de inventario realizados en el restaurante.
 * Cada movimiento de inventario tiene un identificador único, una referencia al almacén afectado,
 * una referencia al usuario que realizó el movimiento, el stock anterior y actual,
 * el tipo de movimiento (entrada, salida, etc.), la fecha del movimiento y un comentario opcional.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="movimientos_inventario")
@Entity
public class Movimientos_Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "almacen_id",nullable = true)
    private Almacen almacen;

    private String nombreMateria;

    @ManyToOne
    @JoinColumn(name = "usuario_id",nullable = true)
    private Usuario usuario;

    private double stockAnterior;

    private double stockActual;

    private String tipoMovimiento;

    private LocalDate fechaMovimiento=LocalDate.now();

    private String comentario="Sin comentarios.";



}

package com.residencia.restaurante.proyecto.entity;

import com.residencia.restaurante.security.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
/**
 * Representa el inventario de materias primas en el restaurante.
 * Cada entrada en el inventario tiene un identificador único,
 * una materia prima asociada, un almacenamiento, un usuario responsable,
 * y los niveles de stock máximo, mínimo y actual.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="inventario")
@Entity
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "materiaPrima_id",nullable = true)
    private MateriaPrima materiaPrima;
    @ManyToOne
    @JoinColumn(name = "almacen_id",nullable = true)
    private Almacen almacen;
    @ManyToOne
    @JoinColumn(name = "usuario_id",nullable = true)
    private Usuario usuario;
    private double stockMax;
    private double stockMin;
    private double stockActual=0;
    @Column(nullable = true)
    private LocalDate fechaIngreso=LocalDate.now();
    @Column(nullable = true)
    private LocalDate fechaUltimoMovimiento=LocalDate.now();
    @Column(nullable = true)
    private String comentario="Sin comentarios";
}

package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa los saldos registrados durante un arqueo de caja para un medio de pago específico.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="arqueoSaldos")
@Entity
public class ArqueoSaldos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = true,name = "arqueo_id")
    private Arqueo arqueo;

    @ManyToOne
    @JoinColumn(nullable = true,name = "medioPago_id")
    private MedioPago medioPago;
    @Column(nullable = true)
    private  double saldoSistema;
    @Column(nullable = true)
    private  double saldoAnotado;
}

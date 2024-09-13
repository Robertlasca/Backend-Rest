package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa un área de servicio en el sistema del restaurante.
 * Contiene información sobre el nombre y la disponibilidad del área de servicio.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="areaServicio")
@Entity
public class AreaServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30, nullable = true)
    private  String nombre;
    @Column(nullable = true)
    private  boolean disponibilidad=true;



}

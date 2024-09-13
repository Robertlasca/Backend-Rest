package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa un almacén en el sistema del restaurante.
 * Contiene información sobre el nombre, la descripción, la visibilidad y la cocina asociada al almacén.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="almacen")
@Entity
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 30, nullable = true)
    private String nombre;
    @Column(length = 50, nullable = true)
    private  String descripcion;
    @Column(nullable = true)
    private boolean visibilidad=true;
    @OneToOne
    @JoinColumn(nullable = true,name = "cocina_id")
    private Cocina cocina;



}

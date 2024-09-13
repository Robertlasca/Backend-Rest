package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="cocina_utensilio")
@Entity
public class Cocina_Utensilio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = true,name = "cocina_id")
    private Cocina cocina;
    @ManyToOne
    @JoinColumn(nullable = true,name = "utensilio_id")
    private Utensilio utensilio;
    private int cantidad;
}

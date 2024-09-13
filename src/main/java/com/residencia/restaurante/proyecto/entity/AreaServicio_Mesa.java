package com.residencia.restaurante.proyecto.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Representa la relación entre un área de servicio y una mesa en el sistema del restaurante.
 * Contiene información sobre la asociación entre un área de servicio y una mesa, así como la posición de la mesa en el área de servicio.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Table(name="areaServicio_Mesa")
@Entity
public class AreaServicio_Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false,name = "areaServicio_id")
    private AreaServicio areaServicio;

    @ManyToOne
    @JoinColumn(nullable = false,name = "mesa_id")
    private  Mesa mesa;

    private int posicionX;
    private int posicionY;

}

package com.residencia.restaurante.proyecto.wrapper;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class IngredientesProductoTerminadoWrapper {
    private double cantidad;
    private Integer idMateriaPrima;
}

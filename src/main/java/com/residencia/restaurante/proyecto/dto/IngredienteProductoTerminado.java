package com.residencia.restaurante.proyecto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class IngredienteProductoTerminado {
    Integer id;
    String nombre;
    double costoProduccion;
    String unidadMedida;
}

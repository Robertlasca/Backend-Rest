package com.residencia.restaurante.proyecto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class RecetaDTO {
    private Integer id;
    private String nombre;
    private double costoProduccion;
    private double cantidad;
    private String unidadMedida;
    private boolean esIngrediente;
    private Integer idProductoTerminado;
}

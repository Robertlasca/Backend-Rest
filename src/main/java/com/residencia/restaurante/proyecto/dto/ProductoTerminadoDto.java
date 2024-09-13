package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.ProductoTerminado;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ProductoTerminadoDto {
    private ProductoTerminado productoTerminado;
    private String estado;
    private String disponibilidad;
    private double costoProduccion;
}

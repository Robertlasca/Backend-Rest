package com.residencia.restaurante.proyecto.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class MovimientoInventarioDTO {
    private String nombreMateria;
    private String nombreAlmacen;
    private double stockAnterior;
    private double stockActual;
    private double diferencia;
    private LocalDate fecha;

}

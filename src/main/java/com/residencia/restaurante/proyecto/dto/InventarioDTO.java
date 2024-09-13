package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.Inventario;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para representar un inventario con su estado y lograr enviar el estado en un mismo e json.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class InventarioDTO {
    private Integer id;
    private String nombreMateria;
    private String nombreAlmacen;
    private double stockActual;
    private double stockMinimo;
    private double stockMaximo;
    private double costoUnitario;
    private double costoTotal;
    private String fechaUtimoMovimiento;
    private String estado;
    private String unidadMedida;
    private Integer idAlmacen;

}

package com.residencia.restaurante.proyecto.wrapper;

import lombok.*;
/**
 * Wrapper para representar la información del inventario de un almacén.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class InventarioWrapper {
    private Integer almacenId;
    private double stockMinimo;
    private double stockMaximo;

}

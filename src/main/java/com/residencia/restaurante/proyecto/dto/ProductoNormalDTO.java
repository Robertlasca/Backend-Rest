package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.ProductoNormal;
import lombok.*;

/**
 * DTO (Data Transfer Object) para representar un producto normal con su estado.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ProductoNormalDTO {
    private ProductoNormal productoNormal;

    private String estado;
    private String disponibilidad;

}

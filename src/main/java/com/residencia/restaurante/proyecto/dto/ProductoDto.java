package com.residencia.restaurante.proyecto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ProductoDto {
    private Integer id;
    private double precio;
    private String nombre;
    private boolean isMenu;
    private String imagen;
}

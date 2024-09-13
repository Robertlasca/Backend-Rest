package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.Categoria;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class CategoriaDTO {
    Categoria categoria;
    String estado;
    int cantidadProductos;
}


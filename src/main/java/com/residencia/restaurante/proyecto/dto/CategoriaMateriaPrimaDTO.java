package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.CategoriaMateriaPrima;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class CategoriaMateriaPrimaDTO {
    CategoriaMateriaPrima categoriaMateriaPrima;
    String estado;
    int cantidadMaterias;
}

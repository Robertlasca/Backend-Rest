package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.Almacen;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AlmacenDTO {
    Almacen almacen;
    String estado;
}

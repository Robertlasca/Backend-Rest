package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.AreaServicio;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AreaServicioDTO {
    AreaServicio areaServicio;
    String estado;
    int cantidadMesas;
}

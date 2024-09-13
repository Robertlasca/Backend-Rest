package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.Caja;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class CajaDTO {
    Caja caja;
    String estado;
}

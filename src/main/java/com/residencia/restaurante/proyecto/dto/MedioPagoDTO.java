package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.MedioPago;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class MedioPagoDTO {
    MedioPago medioPago;
    String estado;
}

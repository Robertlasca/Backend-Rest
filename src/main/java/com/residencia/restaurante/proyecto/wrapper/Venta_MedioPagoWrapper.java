package com.residencia.restaurante.proyecto.wrapper;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Venta_MedioPagoWrapper {
    private Integer id;
    private double pagoRecibido;

}

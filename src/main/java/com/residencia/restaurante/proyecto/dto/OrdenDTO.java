package com.residencia.restaurante.proyecto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class OrdenDTO {
    Integer idOrden;
    int folio;
    String nombreCliente;
    String nombreArea;
    double total;
    String estado;
}

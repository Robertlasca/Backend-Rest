package com.residencia.restaurante.proyecto.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DatosOrdenesDTO {
    String nombrePlatillo;
    int cantidadPlatillo;
    List<DetalleComandaDTO> detalleOrdenProductoDTOS;

}

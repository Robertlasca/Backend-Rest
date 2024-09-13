package com.residencia.restaurante.proyecto.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DatosComandaDTO {
    String areaServicio;
    String nombreCliente;
    String nombreMesa;
    int cantidadPlatillo;
    List<DetalleOrdenProductoDTO> detalleOrdenProductoDTOS;
}

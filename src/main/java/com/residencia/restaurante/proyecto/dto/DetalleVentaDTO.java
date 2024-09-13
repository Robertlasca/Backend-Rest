package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.Venta_MedioPago;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DetalleVentaDTO {
    private VentasDTO ventasDTO;
    private List<DetalleOrdenProductoDTO> detalleOrdenProductoDTOS;
    private List<Venta_MedioPago> ventaMedioPagos;
    private String caja;
    private int comensales;
}

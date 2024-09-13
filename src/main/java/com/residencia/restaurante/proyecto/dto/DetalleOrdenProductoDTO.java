package com.residencia.restaurante.proyecto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DetalleOrdenProductoDTO {
    Integer idDetalleOrden;
    Integer idProducto;
    int cantidad;
    double total;
    String nombreProducto;
    String comentario;
    String estado;
    String esDetalleMenu;
    double precioUnitario;
}

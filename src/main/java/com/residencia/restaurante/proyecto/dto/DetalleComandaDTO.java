package com.residencia.restaurante.proyecto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DetalleComandaDTO {

    Integer idDetalleOrden;
    Integer idProducto;
    int cantidad;
    String comentario;
    String estado;
    String esDetalleMenu;
    String areaServicio;
    String mesa;
    String nombreMesero;
    double precioUnitario;
}

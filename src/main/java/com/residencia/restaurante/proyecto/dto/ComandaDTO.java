package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.DetalleOrdenMenu;
import com.residencia.restaurante.proyecto.entity.DetalleOrden_ProductoNormal;
import com.residencia.restaurante.proyecto.entity.Orden;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ComandaDTO {
    Orden orden;
    List<DetalleOrdenProductoDTO> detalleOrdenProductoDTOS;
    double total;

}

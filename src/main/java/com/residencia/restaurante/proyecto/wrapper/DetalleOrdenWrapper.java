package com.residencia.restaurante.proyecto.wrapper;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DetalleOrdenWrapper {
    private Integer  idProducto;
    private int cantidad;
    private String isMenu;
    private String comentario;
}


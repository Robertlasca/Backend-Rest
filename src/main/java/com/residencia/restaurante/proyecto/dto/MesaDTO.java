package com.residencia.restaurante.proyecto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class MesaDTO {
    Integer id;
    //Coord y
    Double row;
    //Coord x
    Double column;
    String type;
    String name;
    String estado;

}

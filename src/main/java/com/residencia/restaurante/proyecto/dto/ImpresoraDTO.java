package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.Impresora;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ImpresoraDTO {
    Impresora impresora;
    String estado;
}

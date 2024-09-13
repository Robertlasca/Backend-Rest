package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.Menu;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class MenuDTO {
    private Menu menu;
    private String disponibilidad;
    private double ganancia;
}

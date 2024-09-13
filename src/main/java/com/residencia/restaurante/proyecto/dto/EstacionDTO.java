package com.residencia.restaurante.proyecto.dto;

import com.residencia.restaurante.proyecto.entity.Cocina;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class EstacionDTO {
    Cocina cocina;
    String estado;
}

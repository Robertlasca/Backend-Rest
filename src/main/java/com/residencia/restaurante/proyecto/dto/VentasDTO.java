package com.residencia.restaurante.proyecto.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class VentasDTO {
    private Integer id;
    private double subTotal;
    private  double descuento;
    private  double totalPagar;
    private String comentario;
    private LocalDateTime fechaHora;
    private LocalDateTime fechaHoraApertura;
    private String estado;
    private Integer ordenId;
    private String areaServicio;
    private String mesa;
    private String cliente;
    private String usuario;
}

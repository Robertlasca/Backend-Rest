package com.residencia.restaurante.proyecto.wrapper;

import lombok.*;
/**
 * Wrapper para representar los saldos anotados en un arqueo por medio de pago.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ArqueoSaldosWrapper {
    Integer mediopago;
    double saldoAnotado;
}

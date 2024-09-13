package com.residencia.restaurante.security.utils;
import com.residencia.restaurante.proyecto.entity.Impresora;
import com.residencia.restaurante.proyecto.repository.IImpresoraRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class TicketComanda {

    private String contentTicket;

    public TicketComanda(String customerName, String serviceArea, String waiter, List<String> products) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(new Date());
        String time = timeFormat.format(new Date());

        // Caracteres de control para estilos de texto y formato de impresión
        String esc = "\u001B";
        String gs = "\u001D";
        String reset = esc + "@";  // Reset
        String doubleHeightWidthOn = gs + "!" + "\u0011";  // Doble altura y anchura
        String doubleHeightWidthOff = gs + "!" + "\u0000";  // Tamaño normal
        String invertOn = gs + "B" + "\u0001";  // Invertir color (fondo negro, texto blanco)
        String invertOff = gs + "B" + "\u0000";  // Color normal (fondo blanco, texto negro)
        String alignCenter = esc + "a" + "\u0001";  // Alinear al centro
        String alignRight = esc + "a" + "\u0002";  // Alinear a la derecha
        String alignLeft = esc + "a" + "\u0000";  // Alinear a la izquierda

        this.contentTicket =
                reset +
                        alignCenter + invertOn + doubleHeightWidthOn + customerName+"("+serviceArea+")" + doubleHeightWidthOff + invertOff + "\n" +
                        alignRight + "Fecha: " + date + "  Hora: " + time + "\n" +
                        alignLeft + "Mesero: " + waiter + "\n" +
                        "=============================\n" +
                        alignCenter + invertOn + doubleHeightWidthOn + "PRODUCTOS" + doubleHeightWidthOff + invertOff + "\n" +
                        "=============================\n" +
                        String.join("\n", products) + "\n" +
                        "=============================\n" +
                        reset;
    }
}
package com.residencia.restaurante.security.utils;

import lombok.*;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class TicketOrden {

    private String contentTicket;

    String folio;
    String cashierBox;
    String cashier;
    String customerName;
    List<String[]> products;

    String subtotal;
    String discount;
    String numOfProducts;
    String total;
    String received;
    String change;
    List<String[]> paymentMethods;

    public TicketOrden(String folio, String cashierBox, String cashier, String customerName, List<String[]> products,
                       String subtotal, String discount, String numOfProducts, String total, String received, String change,
                       List<String[]> paymentMethods) {


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(new Date());
        String time = timeFormat.format(new Date());

        StringBuilder productsStringBuilder = new StringBuilder();
        for (String[] product : products) {
            String quantity = product[0];
            String productName = product[1];
            String amount = product[2];
            productsStringBuilder.append(String.format("%-10s %-10s %5s\n", quantity, productName, amount));
        }

        StringBuilder paymentMethodsStringBuilder = new StringBuilder();
        for (String[] payment : paymentMethods) {
            String method = payment[0];
            String amount = payment[1];
            paymentMethodsStringBuilder.append(String.format("Pagado con: %-20s Cantidad: %s\n", method, amount));
        }

        this.contentTicket =
                "Plazita Gourmet\n" +
                        "Priv Miguel Hidalgo 16 COLONIA Primera Seccion\n" +
                        "Municipio San Pablo Etla C.P 68258\n\n" +
                        String.format("Fecha: %-40s Hora: %s\n", date, time) +
                        String.format("Folio: %-40s Caja: %s\n", folio, cashierBox) +
                        String.format("Cajero: %s\n", cashier) +
                        String.format("Cliente: %s\n\n", customerName) +
                        "=============================\n" +
                        "PRODUCTOS\n" +
                        "=============================\n" +
                        String.format("%-10s %-10s %5s\n", "Cant", "Nombre", "Importe") +
                        "------------------------------\n" +
                        productsStringBuilder.toString() +
                        "=============================\n" +
                        String.format("Subtotal: %s\n", subtotal) +
                        String.format("Descuento: %s\n", discount) +
                        String.format("N° de productos: %s\n\n", numOfProducts) +
                        String.format("Total: %s\n", total) +
                        String.format("Recibido: %s\n", received) +
                        String.format("Cambio: %s\n\n", change) +
                        paymentMethodsStringBuilder.toString() +
                        "No es un comprobante con valor fiscal.\n" +
                        "¡Gracias por su compra!\n";
    }

    public void print(PrintService selectedService) {
        try {
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            byte[] bytes = this.contentTicket.getBytes();
            Doc doc = new SimpleDoc(bytes, flavor, null);
            DocPrintJob job = selectedService.createPrintJob();
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            job.print(doc, pras);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Folio: ").append(folio).append("\n");
        sb.append("Caja: ").append(cashierBox).append("\n");
        sb.append("Usuario: ").append(cashier).append("\n");
        sb.append("Cliente: ").append(customerName).append("\n");
        sb.append("Productos: \n");
        for (String[] product : products) {
            sb.append(product[0]).append(" x ").append(product[1]).append(" - ").append(product[2]).append("\n");
        }
        sb.append("Subtotal: ").append(subtotal).append("\n");
        sb.append("Descuento: ").append(discount).append("\n");
        sb.append("Total a pagar: ").append(total).append("\n");
        sb.append("Recibido: ").append(received).append("\n");
        sb.append("Cambio: ").append(change).append("\n");
        sb.append("Métodos de pago: \n");
        for (String[] payment : paymentMethods) {
            sb.append(payment[0]).append(": ").append(payment[1]).append("\n");
        }
        return sb.toString();
    }




}

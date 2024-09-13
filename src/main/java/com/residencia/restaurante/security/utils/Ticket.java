package com.residencia.restaurante.security.utils;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;

public class Ticket {

    // Atributo de contenido del ticket
    private String contentTicket = "VINATERIA {{nameLocal}}\n"+
            "EXPEDIDO EN: {{expedition}}\n"+
            "DOMICILIO CONOCIDO MERIDA, YUC.\n"+
            "=============================\n"+
            "MERIDA, XXXXXXXXXXXX\n"+
            "RFC: XXX-020226-XX9\n"+
            "Caja # {{box}} - Ticket # {{ticket}}\n"+
            "LE ATENDIO: {{cajero}}\n"+
            "{{dateTime}}\n"+
            "=============================\n"+
            "{{items}}\n"+
            "=============================\n"+
            "SUBTOTAL: {{subTotal}}\n"+
            "IVA: {{tax}}\n"+
            "TOTAL: {{total}}\n\n"+
            "RECIBIDO: {{recibo}}\n"+
            "CAMBIO: {{change}}\n\n"+
            "=============================\n"+
            "GRACIAS POR SU COMPRA...\n"+
            "ESPERAMOS SU VISITA NUEVAMENTE {{nameLocal}}\n"+
            "\n"+
            "\n";

    // Constructor que establece los valores a la instancia
    public Ticket(String nameLocal, String expedition, String box, String ticket, String caissier, String dateTime, String items, String subTotal, String tax, String total, String recibo, String change) {
        this.contentTicket = this.contentTicket.replace("{{nameLocal}}", nameLocal);
        this.contentTicket = this.contentTicket.replace("{{expedition}}", expedition);
        this.contentTicket = this.contentTicket.replace("{{box}}", box);
        this.contentTicket = this.contentTicket.replace("{{ticket}}", ticket);
        this.contentTicket = this.contentTicket.replace("{{cajero}}", caissier);
        this.contentTicket = this.contentTicket.replace("{{dateTime}}", dateTime);
        this.contentTicket = this.contentTicket.replace("{{items}}", items);
        this.contentTicket = this.contentTicket.replace("{{subTotal}}", subTotal);
        this.contentTicket = this.contentTicket.replace("{{tax}}", tax);
        this.contentTicket = this.contentTicket.replace("{{total}}", total);
        this.contentTicket = this.contentTicket.replace("{{recibo}}", recibo);
        this.contentTicket = this.contentTicket.replace("{{change}}", change);
    }

    public void print() {
        // Especificamos el tipo de datos a imprimir: bytes, autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        // Obtenemos todos los servicios de impresión disponibles
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, null);

        // Buscar el servicio de impresión correspondiente al puerto USB
        PrintService selectedService = null;
        for (PrintService service : printServices) {
            if (service.getName().equalsIgnoreCase("POS-58") || service.getName().contains("USB001")) {
                selectedService = service;
                break;
            }
        }

        if (selectedService == null) {
            JOptionPane.showMessageDialog(null, "No se encontró la impresora POS-58 en el puerto USB001.");
            return;
        }

        // Creamos un conjunto de atributos de solicitud de impresión
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        // Convertimos el string (cuerpo del ticket) a bytes
        byte[] bytes = this.contentTicket.getBytes();

        // Creamos un documento a imprimir, a él se le appendeará el arreglo de bytes
        Doc doc = new SimpleDoc(bytes, flavor, null);
//Hemos llegado
        // Creamos un trabajo de impresión
        DocPrintJob job = selectedService.createPrintJob();

        // Imprimimos dentro de un try-catch para manejar errores
        try {
            job.print(doc, pras);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + e.getMessage());
        }
    }
}
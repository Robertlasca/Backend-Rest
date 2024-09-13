package com.residencia.restaurante.security.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

@RestController
@RequestMapping("/print")
public class PrintServer {

    @PostMapping
    public ResponseEntity<String> print(@RequestBody Map<String, String> printRequest) {
        try {
            String ticketContent = printRequest.get("ticketContent");
            System.out.println(ticketContent+"Contenido");
            String printerIp = printRequest.get("printerIp");
            String nombre = printRequest.get("nombreIm");

            // Lógica para imprimir el ticket
            boolean success;
            //if (printerIp != null && !printerIp.isEmpty()) {
            //success = printTicketByIp(ticketContent, printerIp, 9100);
            //} else {
            success = printTicketByName(ticketContent, nombre);
            //}

            if (success) {
                return new ResponseEntity<>("Impreso correctamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error al imprimir", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al imprimir: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean printTicketByIp(String ticketContent, String printerIp, int printerPort) {
        try (Socket socket = new Socket(printerIp, printerPort)) {
            OutputStream out = socket.getOutputStream();
            out.write(ticketContent.getBytes());
            out.flush();

            // Aquí enviamos el comando de corte automático
            String cutCommand = "\n" + (char)29 + (char)86 + (char)66 + (char)0;
            out.write(cutCommand.getBytes());
            out.flush();

            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean printTicketByName(String ticketContent, String printerName) {
        try {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, pras);

            PrintService selectedService = null;
            for (PrintService printService : printServices) {
                if (printService.getName().equalsIgnoreCase(printerName)) {
                    selectedService = printService;
                    break;
                }
            }

            if (selectedService == null) {
                throw new Exception("No se encontró la impresora con nombre: " + printerName);
            }

            // Comando de corte automático (puede variar según el modelo de la impresora)
            String cutCommand = "\n" + (char)29 + (char)86 + (char)66 + (char)0;

            // Concatenar el comando de corte automático al contenido del ticket
            String finalContent = ticketContent + cutCommand;

            DocPrintJob job = selectedService.createPrintJob();
            Doc doc = new SimpleDoc(finalContent.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
            job.print(doc, null);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.controller.IReporteController;
import com.residencia.restaurante.proyecto.service.IReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;


@RestController
public class ReporteControllerImpl implements IReporteController {
    @Autowired
    private IReporteService reporteService;

    @Override
    public ResponseEntity<byte[]> descargarReporteProductosAgotandose(Map<String,String> objetoMap) {
        ByteArrayInputStream streamReporte = reporteService.generarReporteProductosAgotandose(objetoMap);

        byte[] datosPdf = streamReporte.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_productos_agotandose.pdf");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(datosPdf);
    }

    @Override
    public ResponseEntity<String> handleWebhook() {

        // Procesa los datos recibidos del webhook
        // Aquí puedes incluir la lógica para extraer el contenido del documento a imprimir del payload

        // Envía la solicitud de impresión a la impresora
        boolean impresionExitosa = enviarSolicitudDeImpresion();

        if (impresionExitosa) {
            return ResponseEntity.ok("Impresión exitosa");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al imprimir el documento");
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadPDF(Map<String, String> objetoMap) {
        return reporteService.downloadPDF(objetoMap);
    }

    @Override
    public ResponseEntity<InputStreamResource> descargarReporteInventarioAgotandose(Map<String, String> objetoMap) {
        return reporteService.descargarReporteInventarioAgotandose(objetoMap);
    }

    @Override
    public ResponseEntity<InputStreamResource> descargarReporteInventarioAgotandoseXAlmacen(Map<String, String> objetoMap) {
        return reporteService.descargarReporteInventarioAgotandoseXAlmacen(objetoMap);
    }


    private boolean enviarSolicitudDeImpresion() {
        try {
            // Crear una instancia de RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Definir la URL de la impresora
            String printerUrl = "http://189.250.64.104:9100";


            // Configurar los encabezados de la solicitud
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Crear la entidad de la solicitud con el contenido del documento a imprimir
            HttpEntity<String> requestEntity = new HttpEntity<>("hola", headers);

            // Enviar la solicitud POST a la impresora y obtener la respuesta
            ResponseEntity<String> response = restTemplate.exchange(printerUrl, HttpMethod.POST, requestEntity, String.class);

            // Verificar el código de estado de la respuesta
            if (response.getStatusCode() == HttpStatus.OK) {
                return true; // Impresión exitosa
            } else {
                return false; // Error al imprimir
            }
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir durante la comunicación con la impresora
            e.printStackTrace();
            return false; // Error al imprimir
        }
    }





}

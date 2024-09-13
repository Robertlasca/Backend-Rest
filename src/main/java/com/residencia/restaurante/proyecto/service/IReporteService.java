package com.residencia.restaurante.proyecto.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.util.Map;

public interface IReporteService {
    public ByteArrayInputStream generarReporteProductosAgotandose(Map<String,String> objetoMap);

    public ResponseEntity<InputStreamResource> downloadPDF(Map<String,String> objetoMap);

    public ResponseEntity<InputStreamResource> descargarReporteInventarioAgotandose(Map<String, String> objetoMap);

    public ResponseEntity<InputStreamResource> descargarReporteInventarioAgotandoseXAlmacen(Map<String, String> objetoMap);

    //Ventas
    public ResponseEntity<InputStreamResource> descargarReporteDiarioVentas(Map<String, String> objetoMap);

    public ResponseEntity<InputStreamResource> descargarReporteDiarioSemanal(Map<String, String> objetoMap);

    public ResponseEntity<InputStreamResource> descargarReporteDiarioMensual(Map<String, String> objetoMap);

    public ResponseEntity<InputStreamResource> descargarReporteAnualVentas(Map<String, String> objetoMap);
}

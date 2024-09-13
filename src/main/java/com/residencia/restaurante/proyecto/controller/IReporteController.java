package com.residencia.restaurante.proyecto.controller;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api/reportes")
public interface IReporteController {

    @GetMapping("/productos-agotandose")
    public ResponseEntity<byte[]> descargarReporteProductosAgotandose(@RequestBody(required = true)Map<String,String> objetoMap);


    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook();

    @GetMapping("/descargar")
    public ResponseEntity<InputStreamResource> downloadPDF(@RequestBody(required = true) Map<String,String> objetoMap);

    @GetMapping("/inventario-agotado")
    public  ResponseEntity<InputStreamResource> descargarReporteInventarioAgotandose(@RequestBody(required = true) Map<String,String> objetoMap);

    @GetMapping("/inventario-agotado-almacen")
    public  ResponseEntity<InputStreamResource> descargarReporteInventarioAgotandoseXAlmacen(@RequestBody(required = true) Map<String,String> objetoMap);
}
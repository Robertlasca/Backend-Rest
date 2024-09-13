package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.entity.MovimientosCaja;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface IMovimientoCajaService {
    ResponseEntity<List<MovimientosCaja>> filtarXtipoMovimiento(String tipo);

    ResponseEntity<List<MovimientosCaja>> obtenerMovimientos();

    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    ResponseEntity<String> actualizar(Map<String,String> objetoMap);

    ResponseEntity<String> eliminar(Integer id);

    ResponseEntity<List<MovimientosCaja>> filtarXArqueo(Integer id);

    ResponseEntity<MovimientosCaja> obtenerXid(Integer id);
}

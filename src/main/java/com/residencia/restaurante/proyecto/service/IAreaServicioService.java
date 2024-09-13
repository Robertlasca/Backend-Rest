package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.AreaServicioDTO;
import com.residencia.restaurante.proyecto.entity.AreaServicio;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.Impresora;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface IAreaServicioService {
    ResponseEntity<List<AreaServicioDTO>> obtenerAreasActivas();

    ResponseEntity<List<AreaServicioDTO>> obtenerAreasNoActivas();

    ResponseEntity<List<AreaServicioDTO>> obtenerAreas();

    ResponseEntity<String> cambiarEstado(@RequestBody(required = true) Map<String,String> objetoMap);

    ResponseEntity<String> agregar(@RequestBody(required = true) Map<String,String> objetoMap);

    ResponseEntity<String> actualizar(@RequestBody(required = true) Map<String,String> objetoMap);

    ResponseEntity<AreaServicio> obtenerAreaId(@PathVariable Integer id);

    ResponseEntity<String> eliminar();
}

package com.residencia.restaurante.proyecto.service;

import com.residencia.restaurante.proyecto.dto.MesaDTO;
import com.residencia.restaurante.proyecto.entity.Caja;
import com.residencia.restaurante.proyecto.entity.Mesa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface IMesaService {
    ResponseEntity<List<MesaDTO>> obtenerMesasActivasPorArea(Integer id);
    ResponseEntity<List<Mesa>> obtenerMesasNoActivasPorArea(Integer id);
    ResponseEntity<List<Mesa>> obtenerMesasPorArea(Integer id);

    ResponseEntity<String> cambiarEstado(Integer id);

    ResponseEntity<String> agregar(Map<String,String> objetoMap);

    ResponseEntity<String> actualizar(Map<String,String> objetoMap);


    ResponseEntity<String> mover(@RequestBody(required = true) Map<String,String> objetoMap);


    ResponseEntity<Mesa> obtenerMesaId(@PathVariable Integer id);
}

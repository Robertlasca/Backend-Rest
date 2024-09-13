package com.residencia.restaurante.proyecto.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IGeneralesService {
    //Metodo para responde cuando se loguea y se le crea un token
    ResponseEntity<String> login(Map<String,String> requestMap );

}

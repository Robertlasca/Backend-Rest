package com.residencia.restaurante.proyecto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/auth")
public interface IGeneralesController {

    @PostMapping(path="/login")
    public ResponseEntity<String> login(@RequestBody(required=true) Map<String, String> objetoMap);
}

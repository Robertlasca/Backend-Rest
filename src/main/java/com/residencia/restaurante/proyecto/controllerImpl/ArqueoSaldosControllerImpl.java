package com.residencia.restaurante.proyecto.controllerImpl;

import com.residencia.restaurante.proyecto.controller.IArqueoSaldosController;
import com.residencia.restaurante.proyecto.entity.ArqueoSaldos;
import com.residencia.restaurante.proyecto.service.IArqueoSaldosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del controlador para la gestión de arqueos de saldos en el restaurante.
 */
@RestController
public class ArqueoSaldosControllerImpl implements IArqueoSaldosController {

    @Autowired
    private IArqueoSaldosService arqueoSaldosService;

    /**
     * Obtiene una lista de arqueos de saldos asociados a un identificador específico.
     *
     * @param id El identificador único del arqueo de saldos.
     * @return ResponseEntity con la lista de arqueos de saldos y el estado HTTP correspondiente.
     */
    @Override
    public ResponseEntity<List<ArqueoSaldos>> obtenerArqueoSaldosId(Integer id) {
        try {
            return arqueoSaldosService.obtenerArqueoSaldosId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

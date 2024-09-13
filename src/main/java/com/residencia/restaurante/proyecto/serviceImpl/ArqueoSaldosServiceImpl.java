package com.residencia.restaurante.proyecto.serviceImpl;

import com.residencia.restaurante.proyecto.entity.ArqueoSaldos;
import com.residencia.restaurante.proyecto.repository.IArqueoSaldosRepository;
import com.residencia.restaurante.proyecto.service.IArqueoSaldosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * Servicio que implementa la interfaz IArqueoSaldosService para ofrecer funcionalidades
 * relacionadas con la gestión de arqueos de saldos.
 */
@Service
public class ArqueoSaldosServiceImpl implements IArqueoSaldosService {
    @Autowired
    private IArqueoSaldosRepository arqueoSaldosRepository;
    /**
     * Obtiene una lista de ArqueoSaldos asociados a un ID de arqueo específico.
     *
     * @param id El ID del arqueo para el cual se quieren obtener los saldos.
     * @return Una ResponseEntity que contiene una lista de ArqueoSaldos o un error, según sea el caso.
     */
    @Override
    public ResponseEntity<List<ArqueoSaldos>> obtenerArqueoSaldosId(Integer id) {
        try {
            return new ResponseEntity<List<ArqueoSaldos>>(arqueoSaldosRepository.findAllByArqueo_Id(id),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<ArqueoSaldos>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

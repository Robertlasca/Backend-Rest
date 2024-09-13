package com.residencia.restaurante.proyecto.serviceImpl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FolioService {
    private int currentFolio = 0;

    public synchronized int getNextFolio() {
        return ++currentFolio;  // Incrementa y retorna el nuevo folio
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Se ejecuta a medianoche todos los días
    public synchronized void resetFolio() {
        currentFolio = 0;  // Resetea el folio a 0
    }
}

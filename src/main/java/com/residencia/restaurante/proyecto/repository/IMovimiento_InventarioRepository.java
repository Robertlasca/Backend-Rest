package com.residencia.restaurante.proyecto.repository;

import com.residencia.restaurante.proyecto.entity.Movimientos_Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMovimiento_InventarioRepository extends JpaRepository<Movimientos_Inventario,Integer> {
}

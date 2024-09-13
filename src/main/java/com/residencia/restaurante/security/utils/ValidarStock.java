package com.residencia.restaurante.security.utils;

import java.util.HashMap;
import java.util.Map;

public class ValidarStock{
    private Map<Integer,Double> datos;

    public ValidarStock(){
        datos= new HashMap<>();
    }

    public boolean agregarSiNoExiste(int id, double cantidad) {
        if (datos.containsKey(id)) {
            System.out.println("El ID " + id + " ya existe. No se puede agregar.");
            return false;
        } else {
            datos.put(id, cantidad);
            return true;
        }
    }

    public void actualizar(int id, double cantidad) {
        if (datos.containsKey(id)) {
            datos.put(id, cantidad);
        } else {
            System.out.println("El ID " + id + " no existe. No se puede actualizar.");
        }
    }

    public Double obtenerCantidadPorId(int id) {
        if (datos.containsKey(id)) {
            return datos.get(id);
        } else {
            System.out.println("El ID " + id + " no existe. No se puede obtener la cantidad.");
            return null; // O puedes retornar un valor por defecto según tu lógica de negocio
        }
    }

    public void eliminar(int id) {
        if (datos.containsKey(id)) {
            datos.remove(id);
        } else {
            System.out.println("El ID " + id + " no existe. No se puede eliminar.");
        }
    }

    public void mostrarTodos() {
        for (Map.Entry<Integer, Double> entry : datos.entrySet()) {
            System.out.println("ID: " + entry.getKey() + ", Cantidad: " + entry.getValue());
        }
    }






}

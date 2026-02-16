/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * @author Afgord
 */
public class Medicamento {

    private int id_medicamento;
    private String nombre;

    public Medicamento() {
    }

    public Medicamento(int id_medicamento, String nombre) {
        this.id_medicamento = id_medicamento;
        this.nombre = nombre;
    }

    public int getId_medicamento() {
        return id_medicamento;
    }

    public void setId_medicamento(int id_medicamento) {
        this.id_medicamento = id_medicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Medicamento{" + "id_medicamento=" + id_medicamento + ", nombre="
                + nombre + '}';
    }

}

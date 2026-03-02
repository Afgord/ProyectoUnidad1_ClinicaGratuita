/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * @author Afgord
 */
public class Tratamiento_Medicamento {

    private int id_tratamiento;
    private int id_medicamento;
    private String indicaciones;

    public Tratamiento_Medicamento() {
    }

    public Tratamiento_Medicamento(int id_tratamiento, int id_medicamento,
            String indicaciones) {
        this.id_tratamiento = id_tratamiento;
        this.id_medicamento = id_medicamento;
        this.indicaciones = indicaciones;
    }

    public int getId_tratamiento() {
        return id_tratamiento;
    }

    public void setId_tratamiento(int id_tratamiento) {
        this.id_tratamiento = id_tratamiento;
    }

    public int getId_medicamento() {
        return id_medicamento;
    }

    public void setId_medicamento(int id_medicamento) {
        this.id_medicamento = id_medicamento;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    @Override
    public String toString() {
        return "Tratamiento_Medicamento{" + "id_tratamiento=" + id_tratamiento
                + ", id_medicamento=" + id_medicamento + ", indicaciones=" + indicaciones + '}';
    }

}

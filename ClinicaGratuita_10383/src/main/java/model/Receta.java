/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Afgord
 */
public class Receta {

    private int id_cita;
    private String paciente;
    private String doctor;
    private String medicamento;
    private String indicaciones;
    private int dias_tratamiento;

    public Receta() {
    }

    public Receta(int id_cita, String paciente, String doctor, String medicamento, String indicaciones, int dias_tratamiento) {
        this.id_cita = id_cita;
        this.paciente = paciente;
        this.doctor = doctor;
        this.medicamento = medicamento;
        this.indicaciones = indicaciones;
        this.dias_tratamiento = dias_tratamiento;
    }

    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public int getDias_tratamiento() {
        return dias_tratamiento;
    }

    public void setDias_tratamiento(int dias_tratamiento) {
        this.dias_tratamiento = dias_tratamiento;
    }

    @Override
    public String toString() {
        return "Receta{" + "id_cita=" + id_cita + ", paciente=" + paciente + ", doctor=" + doctor + ", medicamento=" + medicamento + ", indicaciones=" + indicaciones + ", dias_tratamiento=" + dias_tratamiento + '}';
    }
}

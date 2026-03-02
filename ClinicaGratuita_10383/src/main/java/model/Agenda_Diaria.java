/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Time;

/**
 *
 * @author Afgord
 */
public class Agenda_Diaria {

    private int id_cita;
    private int id_doctor;
    private Time hora;
    private String paciente;
    private int edad;
    private String motivo_consulta;
    private String estado;

    public Agenda_Diaria() {

    }

    public Agenda_Diaria(int id_cita, int id_doctor, Time hora, String paciente, int edad, String motivo_consulta, String estado) {
        this.id_cita = id_cita;
        this.id_doctor = id_doctor;
        this.hora = hora;
        this.paciente = paciente;
        this.edad = edad;
        this.motivo_consulta = motivo_consulta;
        this.estado = estado;
    }

    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(int id_doctor) {
        this.id_doctor = id_doctor;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getMotivo_consulta() {
        return motivo_consulta;
    }

    public void setMotivo_consulta(String motivo_consulta) {
        this.motivo_consulta = motivo_consulta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Agenda_Diaria{" + "id_cita=" + id_cita + ",id_doctor=" + id_doctor + ", hora=" + hora
                + ", paciente=" + paciente + ", edad=" + edad + ", motivo_consulta=" + motivo_consulta
                + ", estado=" + estado + '}';
    }

}

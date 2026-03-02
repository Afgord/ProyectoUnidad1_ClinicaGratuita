/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.sql.Time;

/**
 * Clase Cita_Medica
 * Esta clase pertenece al Modelo del proyecto
 * Se encarga de crear los objetos Cita_medica
 * 
 * @author Afgord
 */
public class Cita_Medica {

    private int id_cita;
    private Date fecha;
    private Time hora;
    private String motivo_consulta;
    private String estado;
    private int id_paciente;
    private int id_doctor;

    /**
     * Tenemos el constructor vacio
     */
    public Cita_Medica() {
    }

    /**
     * Tenemos el constructor del objeto Paciente que recibe como parámetros
     * @param id_cita  El id generado para la cita
     * @param fecha La fecha en que se atenderá la cita
     * @param hora La hora en que se atenderá la cita
     * @param motivo_consulta El motivo de la consulta por parte del paciente
     * @param estado El estado de la consulta
     * @param id_paciente El id del paciente
     * @param id_doctor El id del doctor que atenderá la cita
     */
    public Cita_Medica(int id_cita, Date fecha, Time hora, String motivo_consulta,
            String estado, int id_paciente, int id_doctor) {
        this.id_cita = id_cita;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo_consulta = motivo_consulta;
        this.estado = estado;
        this.id_paciente = id_paciente;
        this.id_doctor = id_doctor;
    }

    // A continuación se enlistan los getter y setters del objeto Cita_Medica para
     // cada uno de sus atributos
    
    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getMotivo_Consulta() {
        return motivo_consulta;
    }

    public void setMotivo_Consulta(String motivo_consulta) {
        this.motivo_consulta = motivo_consulta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(int id_doctor) {
        this.id_doctor = id_doctor;
    }

    @Override
    public String toString() {
        return "Cita_Medica{" + "id_cita=" + id_cita + ", fecha=" + fecha + ", hora=" + hora
                + ", motivo_consulta=" + motivo_consulta + ", estado=" + estado + ", id_paciente=" + id_paciente
                + ", id_doctor=" + id_doctor + '}';
    }

}

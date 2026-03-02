/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * @author Afgord
 */
public class Tratamiento {

    private int id_tratamiento;
    private String descripcion;
    private int duracion;
    private int id_cita;

    public Tratamiento() {
    }

    public Tratamiento(int id_tratamiento, String descripcion, int duracion, int id_cita) {
        this.id_tratamiento = id_tratamiento;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.id_cita = id_cita;
    }

    public int getId_tratamiento() {
        return id_tratamiento;
    }

    public void setId_tratamiento(int id_tratamiento) {
        this.id_tratamiento = id_tratamiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    @Override
    public String toString() {
        return "Tratamiento{" + "id_tratamiento=" + id_tratamiento + ", descripcion="
                + descripcion + ", duracion=" + duracion + ", id_cita=" + id_cita + '}';
    }

}

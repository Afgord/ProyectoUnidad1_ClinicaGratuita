/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * @author Afgord
 */
public class Doctor {

    private int id_doctor;
    private String nombre;
    private String email;
    private String telefono;
    private String especialidad;

    public Doctor() {
    }

    public Doctor(int id_doctor, String nombre, String email, String telefono, String especialidad) {
        this.id_doctor = id_doctor;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.especialidad = especialidad;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(int id_doctor) {
        this.id_doctor = id_doctor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return "Doctor{" + "id_doctor=" + id_doctor + ", nombre=" + nombre
                + ", email=" + email + ", telefono=" + telefono + ", especialidad=" + especialidad + '}';
    }

}

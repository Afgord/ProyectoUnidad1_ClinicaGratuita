/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Clase Doctor Esta clase pertenece al Modelo del proyecto Se encarga de crear
 * los objetos Doctor
 *
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

    /**
     * Tenemos el constructor del objeto Doctor que recibe como parámetros
     *
     * @param id_doctor el id del doctor
     * @param nombre el nombre del doctor
     * @param email el correo electrónico del doctor
     * @param telefono el teléfono actual del doctor
     * @param especialidad la especialidad del doctor
     */
    public Doctor(int id_doctor, String nombre, String email, String telefono, String especialidad) {
        this.id_doctor = id_doctor;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.especialidad = especialidad;
    }

    // A continuación se enlistan los getter y setters del objeto Doctor para
    // cada uno de sus atributos
    
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

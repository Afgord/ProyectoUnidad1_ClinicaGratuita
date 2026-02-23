/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.DoctorDAO;
import interfaces.IDoctorDAO;
import java.util.List;
import model.Doctor;

/**
 *
 * @author Afgord
 */
public class DoctorController {

    private final IDoctorDAO doctorDAO;
    private String ultimo_mensaje;

    public DoctorController() {
        this.doctorDAO = new DoctorDAO();
        this.ultimo_mensaje = "";
    }

    public String getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    public boolean insertarDoctor(String nombre, String email, String telefono, String especialidad) {
        try {
            if (nombre == null || nombre.trim().isEmpty()
                    || email == null || email.trim().isEmpty()
                    || telefono == null || telefono.trim().isEmpty()
                    || especialidad == null || especialidad.trim().isEmpty()) {
                ultimo_mensaje = "Todos los campos son obligatorios.";
                return false;
            }

            String regex_email = "^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
            if (!email.matches(regex_email)) {
                ultimo_mensaje = "Formato de correo inválido.";
                return false;
            }

            if (!telefono.matches("^[0-9]{10}$")) {
                ultimo_mensaje = "El teléfono debe contener 10 dígitos.";
                return false;
            }

            Doctor doctor = new Doctor();
            doctor.setNombre(nombre.trim());
            doctor.setEmail(email.trim().toLowerCase());
            doctor.setTelefono(telefono.trim());
            doctor.setEspecialidad(especialidad.trim());

            boolean ok = doctorDAO.insertar(doctor);
            ultimo_mensaje = ok ? "Doctor registrado correctamente." : "No se pudo registrar el doctor.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }

    public Doctor obtenerPorId(int id_doctor) {
        try {
            if (id_doctor <= 0) {
                ultimo_mensaje = "ID inválido.";
                return null;
            }
            return doctorDAO.obtenerPorId(id_doctor);
        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return null;
        }
    }

    public List<Doctor> obtenerTodos() {
        return doctorDAO.obtenerTodos();
    }

    public boolean actualizar(Doctor doctor) {
        try {
            if (doctor == null) {
                ultimo_mensaje = "Doctor inválido.";
                return false;
            }

            if (doctor.getId_doctor() <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }

            if (doctor.getNombre() == null || doctor.getNombre().trim().isEmpty()
                    || doctor.getEmail() == null || doctor.getEmail().trim().isEmpty()
                    || doctor.getTelefono() == null || doctor.getTelefono().trim().isEmpty()
                    || doctor.getEspecialidad() == null || doctor.getEspecialidad().trim().isEmpty()) {
                ultimo_mensaje = "Nombre, email, teléfono y especialidad son obligatorios.";
                return false;
            }

            if (!doctor.getTelefono().matches("^[0-9]{10}$")) {
                ultimo_mensaje = "Teléfono inválido.";
                return false;
            }

            String regex_email = "^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
            if (!doctor.getEmail().matches(regex_email)) {
                ultimo_mensaje = "Formato de correo inválido.";
                return false;
            }

            doctor.setNombre(doctor.getNombre().trim());
            doctor.setEmail(doctor.getEmail().trim().toLowerCase());
            doctor.setTelefono(doctor.getTelefono().trim());
            doctor.setEspecialidad(doctor.getEspecialidad().trim());

            boolean ok = doctorDAO.actualizar(doctor);
            ultimo_mensaje = ok ? "Doctor actualizado." : "No se pudo actualizar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }

    public boolean eliminar(int id_doctor) {
        try {
            if (id_doctor <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }

            boolean ok = doctorDAO.eliminar(id_doctor);
            ultimo_mensaje = ok ? "Doctor eliminado." : "No se pudo eliminar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }
}

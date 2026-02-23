/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.PacienteDAO;
import interfaces.IPacienteDAO;
import java.util.List;
import model.Paciente;

/**
 *
 * @author Afgord
 */
public class PacienteController {

    private final IPacienteDAO pacienteDAO;
    private String ultimo_mensaje = "";

    public PacienteController() {
        this.pacienteDAO = new PacienteDAO();
    }

    public String getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    /**
     * Regla de Negocio: Validar datos antes de enviarlos al DAO. Retornamos un
     * boolean para que la Vista (JFrame) lo muestre en un JOptionPane.
     */
    public boolean insertarPaciente(String nombre, int edad, String sexo, String direccion, String email, String telefono) {

        try {

            if (nombre == null || nombre.trim().isEmpty()
                    || direccion == null || direccion.trim().isEmpty()
                    || email == null || email.trim().isEmpty()
                    || telefono == null || telefono.trim().isEmpty()) {
                ultimo_mensaje = "Todos los campos son obligatorios.";
                return false;
            }

            if (edad < 0 || edad > 120) {
                ultimo_mensaje = "Edad fuera de rango (0–120).";
                return false;
            }

            if (!sexo.equals("Masculino") && !sexo.equals("Femenino")) {
                ultimo_mensaje = "Sexo inválido. Use Masculino o Femenino.";
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

            Paciente paciente = new Paciente();
            paciente.setNombre(nombre.trim());
            paciente.setEdad(edad);
            paciente.setSexo(sexo);
            paciente.setDireccion(direccion.trim());
            paciente.setEmail(email.trim().toLowerCase());
            paciente.setTelefono(telefono.trim());

            boolean ok = pacienteDAO.insertar(paciente);

            ultimo_mensaje = ok ? "Paciente registrado correctamente." : "No se pudo registrar el paciente.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }

    public Paciente obtenerPorId(int id_paciente) {
        try {
            if (id_paciente <= 0) {
                ultimo_mensaje = "ID inválido.";
                return null;
            }
            return pacienteDAO.obtenerPorId(id_paciente);
        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return null;
        }
    }

    public List<Paciente> obtenerTodos() {
        return pacienteDAO.obtenerTodos();
    }

    public boolean actualizar(Paciente paciente) {
        try {
            if (paciente == null) {
                ultimo_mensaje = "Paciente inválido.";
                return false;
            }

            if (paciente.getId_paciente() <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }

            if (paciente.getDireccion() == null || paciente.getDireccion().trim().isEmpty()
                    || paciente.getEmail() == null || paciente.getEmail().trim().isEmpty()
                    || paciente.getTelefono() == null || paciente.getTelefono().trim().isEmpty()) {
                ultimo_mensaje = "Dirección, email y teléfono son obligatorios.";
                return false;
            }

            if (!paciente.getTelefono().matches("^[0-9]{10}$")) {
                ultimo_mensaje = "Teléfono inválido.";
                return false;
            }

            String regex_email = "^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
            if (!paciente.getEmail().matches(regex_email)) {
                ultimo_mensaje = "Formato de correo inválido.";
                return false;
            }

            paciente.setDireccion(paciente.getDireccion().trim());
            paciente.setEmail(paciente.getEmail().trim().toLowerCase());
            paciente.setTelefono(paciente.getTelefono().trim());

            boolean ok = pacienteDAO.actualizar(paciente);
            ultimo_mensaje = ok ? "Paciente actualizado." : "No se pudo actualizar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }

    public boolean eliminar(int id_paciente) {
        try {
            if (id_paciente <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }

            boolean ok = pacienteDAO.eliminar(id_paciente);
            ultimo_mensaje = ok ? "Paciente eliminado." : "No se pudo eliminar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }
}

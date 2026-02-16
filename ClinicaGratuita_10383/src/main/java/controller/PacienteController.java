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

    public PacienteController() {
        this.pacienteDAO = new PacienteDAO();
    }

    /**
     * Regla de Negocio: Validar datos antes de enviarlos al DAO. Retornamos un
     * String para que la Vista (JFrame) lo muestre en un JOptionPane.
     */
    public boolean insertarPaciente(String nombre, int edad, String sexo, String direccion, String email, String telefono) {

        // 1. Validaciones de presencia (No vacíos)
        if (nombre.trim().isEmpty() || direccion.trim().isEmpty() || email.trim().isEmpty() || telefono.trim().isEmpty()) {
            System.err.println("Error: Todos los campos obligatorios deben ser completados.");
            return false;
        }

        // 2. Validación de Edad (Lógica coincidente con el CHECK de la DB)
        if (edad < 0 || edad > 120) {
            System.err.println("Error: La edad debe ser un rango válido (0-120).");
            return false;
        }

        // 3. Validación de Formato de Email (Regex básica)
        if (!email.contains("@") || !email.contains(".")) {
            System.err.println("Error: El formato del correo electrónico es incorrecto.");
            return false;
        }

        // 4. Validación de Teléfono (Debe ser de 10 dígitos según tu SQL)
        if (telefono.trim().length() != 10 || !telefono.matches("[0-9]+")) {
            System.err.println("Error: El teléfono debe tener exactamente 10 dígitos.");
            return false;
        }

        // Si pasa las validaciones, creamos el modelo
        Paciente paciente = new Paciente();
        paciente.setNombre(nombre.trim());
        paciente.setEdad(edad);
        paciente.setSexo(sexo);
        paciente.setDireccion(direccion.trim());
        paciente.setEmail(email.trim().toLowerCase());
        paciente.setTelefono(telefono.trim());

        return pacienteDAO.insertar(paciente);
    }

    public Paciente obtenerPorId(int id_paciente) {
        if (id_paciente <= 0) {
            System.err.println("Error: El ID debe ser mayor a cero.");
            return null;
        }
        return pacienteDAO.obtenerPorId(id_paciente);
    }

    public List<Paciente> obtenerTodos() {
        return pacienteDAO.obtenerTodos();
    }

    public boolean actualizar(Paciente paciente) {
        // Validaciones de negocio antes de llamar al DAO
        if (paciente.getNombre().trim().isEmpty()
                || paciente.getDireccion().trim().isEmpty()
                || paciente.getEmail().trim().isEmpty()) {
            System.err.println("Error: Hay campos obligatorios vacíos.");
            return false;
        }

        // Validación de edad (conforme al CHECK de la DB)
        if (paciente.getEdad() < 0 || paciente.getEdad() > 120) {
            System.err.println("Error: Edad fuera de rango (0-120).");
            return false;
        }

        // Validación de teléfono (regexp '^[0-9]{10}$')
        if (paciente.getTelefono().trim().length() != 10 || !paciente.getTelefono().matches("[0-9]+")) {
            System.err.println("Error: El teléfono debe tener 10 dígitos numéricos.");
            return false;
        }

        paciente.setNombre(paciente.getNombre().trim());
        paciente.setEmail(paciente.getEmail().trim().toLowerCase());

        return pacienteDAO.actualizar(paciente);
    }

    public boolean eliminar(int id_paciente) {
        if (id_paciente <= 0) {
            System.err.println("Error: ID no válido para eliminación.");
            return false;
        }
        return pacienteDAO.eliminar(id_paciente);
    }
}

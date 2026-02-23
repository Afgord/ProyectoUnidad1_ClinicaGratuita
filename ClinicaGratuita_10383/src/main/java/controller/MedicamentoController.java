/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.MedicamentoDAO;
import interfaces.IMedicamentoDAO;
import java.util.List;
import model.Medicamento;

/**
 *
 * @author Afgord
 */
public class MedicamentoController {

    private final IMedicamentoDAO medicamentoDAO;
    private String ultimo_mensaje = "";

    public MedicamentoController() {
        this.medicamentoDAO = new MedicamentoDAO();
    }

    public String getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    public boolean insertar(String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                ultimo_mensaje = "El nombre del medicamento es obligatorio.";
                return false;
            }

            Medicamento medicamento = new Medicamento();
            medicamento.setNombre(nombre.trim());

            boolean ok = medicamentoDAO.insertar(medicamento);
            ultimo_mensaje = ok ? "Medicamento registrado." : "No se pudo registrar el medicamento.";
            return ok;

        } catch (Exception e) {
            String msg = (e.getMessage() == null) ? "" : e.getMessage().toLowerCase();

            // Por tu constraint: uq_medicamentos_nombre
            if (msg.contains("duplicate") || msg.contains("uq_medicamentos_nombre")) {
                ultimo_mensaje = "El medicamento ya está registrado.";
            } else {
                ultimo_mensaje = e.getMessage();
            }
            return false;
        }
    }

    public Medicamento obtenerPorId(int id_medicamento) {
        try {
            if (id_medicamento <= 0) {
                ultimo_mensaje = "ID inválido.";
                return null;
            }

            Medicamento m = medicamentoDAO.obtenerPorId(id_medicamento);
            ultimo_mensaje = (m != null) ? "Medicamento encontrado." : "No existe el medicamento.";
            return m;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return null;
        }
    }

    public List<Medicamento> obtenerTodos() {
        try {
            List<Medicamento> lista = medicamentoDAO.obtenerTodos();
            ultimo_mensaje = "Medicamentos cargados: " + lista.size();
            return lista;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

    public boolean actualizar(Medicamento medicamento) {
        try {
            if (medicamento == null) {
                ultimo_mensaje = "Medicamento inválido.";
                return false;
            }
            if (medicamento.getId_medicamento() <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }
            if (medicamento.getNombre() == null || medicamento.getNombre().trim().isEmpty()) {
                ultimo_mensaje = "El nombre es obligatorio.";
                return false;
            }

            medicamento.setNombre(medicamento.getNombre().trim());

            boolean ok = medicamentoDAO.actualizar(medicamento);
            ultimo_mensaje = ok ? "Medicamento actualizado." : "No se pudo actualizar.";
            return ok;

        } catch (Exception e) {
            String msg = (e.getMessage() == null) ? "" : e.getMessage().toLowerCase();

            if (msg.contains("duplicate") || msg.contains("uq_medicamentos_nombre")) {
                ultimo_mensaje = "Ya existe un medicamento con ese nombre.";
            } else {
                ultimo_mensaje = e.getMessage();
            }
            return false;
        }
    }

    public boolean eliminar(int id_medicamento) {
        try {
            if (id_medicamento <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }

            boolean ok = medicamentoDAO.eliminar(id_medicamento);
            ultimo_mensaje = ok ? "Medicamento eliminado." : "No se pudo eliminar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }
}

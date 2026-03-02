/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Tratamiento_MedicamentoDAO;
import interfaces.ITratamiento_MedicamentoDAO;
import java.util.List;
import model.Tratamiento_Medicamento;

/**
 *
 * @author Afgord
 */
public class Tratamiento_MedicamentoController {

    private final ITratamiento_MedicamentoDAO relacionDAO;
    private String ultimo_mensaje = "";

    public Tratamiento_MedicamentoController() {
        this.relacionDAO = new Tratamiento_MedicamentoDAO();
    }

    public String getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    public boolean agregarMedicamento(int id_tratamiento, int id_medicamento, String indicaciones) {
        try {
            if (id_tratamiento <= 0) {
                ultimo_mensaje = "ID de tratamiento inválido.";
                return false;
            }
            if (id_medicamento <= 0) {
                ultimo_mensaje = "ID de medicamento inválido.";
                return false;
            }
            if (indicaciones == null || indicaciones.trim().isEmpty()) {
                ultimo_mensaje = "Las indicaciones son obligatorias.";
                return false;
            }

            Tratamiento_Medicamento relacion = new Tratamiento_Medicamento();
            relacion.setId_tratamiento(id_tratamiento);
            relacion.setId_medicamento(id_medicamento);
            relacion.setIndicaciones(indicaciones.trim());

            boolean ok = relacionDAO.insertar(relacion);
            ultimo_mensaje = ok ? "Medicamento agregado a la receta." : "No se pudo agregar el medicamento.";
            return ok;

        } catch (Exception e) {
            String msg = (e.getMessage() == null) ? "" : e.getMessage().toLowerCase();

            // PK compuesta o mensaje del SP
            if (msg.contains("ya se encuentra en la receta") || msg.contains("duplicate")) {
                ultimo_mensaje = "Ese medicamento ya está agregado en la receta.";
            } else {
                ultimo_mensaje = e.getMessage();
            }
            return false;
        }
    }

    public List<Tratamiento_Medicamento> obtenerPorTratamiento(int id_tratamiento) {
        try {
            if (id_tratamiento <= 0) {
                ultimo_mensaje = "ID inválido.";
                return List.of();
            }
            List<Tratamiento_Medicamento> lista = relacionDAO.obtenerPorTratamiento(id_tratamiento);
            ultimo_mensaje = "Receta cargada: " + lista.size();
            return lista;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

    public List<Tratamiento_Medicamento> obtenerPorMedicamento(int id_medicamento) {
        try {
            if (id_medicamento <= 0) {
                ultimo_mensaje = "ID inválido.";
                return List.of();
            }
            List<Tratamiento_Medicamento> lista = relacionDAO.obtenerPorMedicamento(id_medicamento);
            ultimo_mensaje = "Relaciones cargadas: " + lista.size();
            return lista;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

    public boolean eliminar(int id_tratamiento, int id_medicamento) {
        try {
            if (id_tratamiento <= 0 || id_medicamento <= 0) {
                ultimo_mensaje = "IDs inválidos.";
                return false;
            }

            boolean ok = relacionDAO.eliminar(id_tratamiento, id_medicamento);
            ultimo_mensaje = ok ? "Medicamento eliminado de la receta." : "No se pudo eliminar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }

}

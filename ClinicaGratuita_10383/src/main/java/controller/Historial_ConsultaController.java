/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Historial_ConsultaDAO;
import interfaces.IHistorial_ConsultaDAO;
import java.util.List;
import model.Historial_Consulta;

/**
 *
 * @author Afgord
 */
public class Historial_ConsultaController {

    private final IHistorial_ConsultaDAO dao = new Historial_ConsultaDAO();
    private String ultimo_mensaje = "";

    public String getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    public List<Historial_Consulta> obtenerHistorial(int idPaciente) {
        try {
            if (idPaciente <= 0) {
                ultimo_mensaje = "Paciente inválido.";
                return List.of();
            }

            List<Historial_Consulta> lista = dao.obtenerHistorialConsultas(idPaciente);

            ultimo_mensaje = lista.isEmpty()
                    ? "Este paciente no tiene consultas registradas en el historial."
                    : "Historial cargado: " + lista.size();

            return lista;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

}

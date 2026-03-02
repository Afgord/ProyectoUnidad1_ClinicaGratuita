/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.RecetaDAO;
import interfaces.IRecetaDAO;
import java.util.List;
import model.Receta;

/**
 *
 * @author Afgord
 */
public class RecetaController {

    private final IRecetaDAO recetaDAO;
    private String ultimo_mensaje = "";

    public RecetaController() {
        this.recetaDAO = new RecetaDAO();
    }

    public String getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    public List<Receta> obtenerRecetaPorCita(int id_cita) {
        try {
            if (id_cita <= 0) {
                ultimo_mensaje = "ID de cita inválido.";
                return List.of();
            }

            List<Receta> lista = recetaDAO.obtenerPorCita(id_cita);
            ultimo_mensaje = "Receta cargada: " + lista.size();
            return lista;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Agenda_DiariaDAO;
import interfaces.IAgenda_DiariaDAO;
import java.util.List;
import model.Agenda_Diaria;

/**
 *
 * @author Afgord
 */
public class Agenda_DiariaController {

    private final IAgenda_DiariaDAO agendaDAO;
    private String ultimo_mensaje = "";

    public Agenda_DiariaController() {
        this.agendaDAO = new Agenda_DiariaDAO();
    }

    public String getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    public List<Agenda_Diaria> obtenerAgendaHoy() {
        try {
            List<Agenda_Diaria> lista = agendaDAO.obtenerAgendaHoy();
            ultimo_mensaje = lista.isEmpty()
                    ? "No hay citas programadas para hoy."
                    : "Agenda cargada correctamente.";
            return lista;
        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

}

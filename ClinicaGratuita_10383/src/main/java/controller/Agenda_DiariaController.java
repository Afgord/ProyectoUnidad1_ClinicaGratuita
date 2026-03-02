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

    public List<Agenda_Diaria> obtenerAgendaHoyPorDoctor(int idDoctor) {
        try {
            if (idDoctor <= 0) {
                ultimo_mensaje = "Doctor inválido.";
                return List.of();
            }

//            java.time.DayOfWeek dow = java.time.LocalDate.now().getDayOfWeek();
//            if (dow == java.time.DayOfWeek.SATURDAY || dow == java.time.DayOfWeek.SUNDAY) {
//                ultimo_mensaje = "Hoy no hay consultas (solo Lunes a Viernes).";
//                return List.of();
//            }
            List<Agenda_Diaria> lista = agendaDAO.obtenerAgendaHoyPorDoctor(idDoctor);
            ultimo_mensaje = lista.isEmpty()
                    ? "No hay citas programadas para hoy (desde este momento)."
                    : "Agenda cargada correctamente.";
            return lista;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

    public List<Agenda_Diaria> obtenerAgendaParaSeleccion(int idDoctor) {
        try {
            if (idDoctor <= 0) {
                ultimo_mensaje = "Doctor inválido.";
                return List.of();
            }

            // 1) HOY (vista_agenda_diaria): ya filtra por fecha=CURDATE() y estado != cancelada
            List<Agenda_Diaria> hoy = agendaDAO.obtenerAgendaHoyPorDoctor(idDoctor);
            if (!hoy.isEmpty()) {
                ultimo_mensaje = "Agenda de hoy cargada correctamente.";
                return hoy;
            }

            // 2) Fallback controlado (solo controller): próximas citas programadas L-V
            List<Agenda_Diaria> prox = agendaDAO.obtenerProximasCitasPorDoctor(idDoctor);

            if (prox.isEmpty()) {
                ultimo_mensaje = "No hay consultas para hoy ni próximas citas programadas para este doctor.";
            } else {
                ultimo_mensaje = "Hoy no hay consultas para este doctor. Se muestran las próximas citas programadas.";
            }

            return prox;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }
}

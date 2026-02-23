/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Cita_MedicaDAO;
import interfaces.ICita_MedicaDAO;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import model.Cita_Medica;

/**
 *
 * @author Afgord
 */
public class Cita_MedicaController {

    private final ICita_MedicaDAO cita_medicaDAO;
    private String ultimo_mensaje = "";

    public Cita_MedicaController() {
        this.cita_medicaDAO = new Cita_MedicaDAO();
    }

    public String getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    public boolean insertar(Date fecha, Time hora, String motivo_consulta, int id_paciente, int id_doctor) {

        try {
            if (fecha == null || hora == null
                    || motivo_consulta == null || motivo_consulta.trim().isEmpty()) {
                ultimo_mensaje = "Fecha, hora y motivo de consulta son obligatorios.";
                return false;
            }

            if (id_paciente <= 0 || id_doctor <= 0) {
                ultimo_mensaje = "ID de doctor o paciente no válido.";
                return false;
            }

            LocalDate fecha_local = fecha.toLocalDate();
            LocalTime hora_local = hora.toLocalTime();
            LocalDate hoy = LocalDate.now();

            if (fecha_local.isBefore(hoy)) {
                ultimo_mensaje = "La fecha no debe ser en pasado.";
                return false;
            }

            boolean turno_matutino = !hora_local.isBefore(LocalTime.of(7, 0)) && !hora_local.isAfter(LocalTime.of(13, 0));
            boolean turno_vespertino = !hora_local.isBefore(LocalTime.of(15, 0)) && !hora_local.isAfter(LocalTime.of(19, 0));

            if (!(turno_matutino || turno_vespertino)) {
                ultimo_mensaje = "Solo se atiende de 7 AM - 1 PM y 3 PM - 7 PM.";
                return false;
            }

            DayOfWeek dia = fecha_local.getDayOfWeek();
            if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
                ultimo_mensaje = "Solo hay consultas los días Lunes a Viernes.";
                return false;
            }

            Cita_Medica cita = new Cita_Medica();
            cita.setFecha(fecha);
            cita.setHora(hora);
            cita.setMotivo_Consulta(motivo_consulta.trim());
            cita.setId_paciente(id_paciente);
            cita.setId_doctor(id_doctor);

            boolean ok = cita_medicaDAO.insertar(cita);
            ultimo_mensaje = ok ? "Cita registrada correctamente." : "No se pudo registrar la cita.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }

    public Cita_Medica obtenerPorId(int id_cita) {
        try {
            if (id_cita <= 0) {
                ultimo_mensaje = "ID inválido.";
                return null;
            }
            return cita_medicaDAO.obtenerPorId(id_cita);
        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return null;
        }
    }

    public List<Cita_Medica> obtenerTodos() {
        try {
            return cita_medicaDAO.obtenerTodos();
        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

    public List<Cita_Medica> obtenerCitasPorPaciente(int id_paciente) {
        try {
            if (id_paciente <= 0) {
                ultimo_mensaje = "ID inválido.";
                return List.of();
            }
            return cita_medicaDAO.obtenerCitasPorPaciente(id_paciente);
        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

    public List<Cita_Medica> obtenerCitasPorFecha(Date fecha) {
        try {
            if (fecha == null) {
                ultimo_mensaje = "Fecha inválida.";
                return List.of();
            }
            return cita_medicaDAO.obtenerCitasPorFecha(fecha);
        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

    public boolean actualizar(Cita_Medica cita) {
        try {
            if (cita == null) {
                ultimo_mensaje = "Cita inválida.";
                return false;
            }

            if (cita.getId_cita() <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }

            if (cita.getFecha() == null || cita.getHora() == null
                    || cita.getMotivo_Consulta() == null || cita.getMotivo_Consulta().trim().isEmpty()) {
                ultimo_mensaje = "Fecha, hora y motivo son obligatorios.";
                return false;
            }

            if (cita.getId_paciente() <= 0 || cita.getId_doctor() <= 0) {
                ultimo_mensaje = "ID de paciente o doctor inválido.";
                return false;
            }

            if (cita.getEstado() == null || cita.getEstado().trim().isEmpty()) {
                ultimo_mensaje = "Estado obligatorio.";
                return false;
            }

            List<String> estados_permitidos = Arrays.asList("programada", "en curso", "completada", "cancelada");
            if (!estados_permitidos.contains(cita.getEstado().trim().toLowerCase())) {
                ultimo_mensaje = "Estado inválido. Las opciones son: programada, en curso, completada o cancelada.";
                return false;
            }

            cita.setMotivo_Consulta(cita.getMotivo_Consulta().trim());
            cita.setEstado(cita.getEstado().trim().toLowerCase());

            boolean ok = cita_medicaDAO.actualizar(cita);
            ultimo_mensaje = ok ? "Cita actualizada." : "No se pudo actualizar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }

    public boolean eliminar(int id_cita) {
        try {
            if (id_cita <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }

            boolean ok = cita_medicaDAO.eliminar(id_cita);
            ultimo_mensaje = ok ? "Cita eliminada." : "No se pudo eliminar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }
}

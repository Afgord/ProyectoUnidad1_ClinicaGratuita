/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.TratamientoDAO;
import interfaces.ITratamientoDAO;
import java.util.List;
import model.Tratamiento;

/**
 *
 * @author Afgord
 */
public class TratamientoController {

    private final ITratamientoDAO tratamientoDAO;
    private String ultimo_mensaje = "";

    private int ultimo_id_tratamiento = -1;

    public TratamientoController() {
        this.tratamientoDAO = new TratamientoDAO();
    }

    public String getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    public int getUltimo_id_tratamiento() {
        return ultimo_id_tratamiento;
    }

    // Regla de negocio: finalizar consulta = crear tratamiento + completar cita (SP)
    public boolean finalizarConsulta(int id_cita, String descripcion, int duracion) {
        try {
            if (id_cita <= 0) {
                ultimo_mensaje = "ID de cita inválido.";
                return false;
            }
            if (descripcion == null || descripcion.trim().isEmpty()) {
                ultimo_mensaje = "La descripción del tratamiento es obligatoria.";
                return false;
            }
            if (duracion <= 0) {
                ultimo_mensaje = "La duración debe ser mayor a 0 días.";
                return false;
            }

            Tratamiento tratamiento = new Tratamiento();
            tratamiento.setId_cita(id_cita);
            tratamiento.setDescripcion(descripcion.trim());
            tratamiento.setDuracion(duracion);

            boolean ok = tratamientoDAO.insertar(tratamiento);

            if (ok && tratamiento.getId_tratamiento() <= 0) {
                ok = false;
            }

            ultimo_id_tratamiento = ok ? tratamiento.getId_tratamiento() : -1;

            // Si el SP devolvió el id, ya quedó guardado en t.setId_tratamiento(...)
            ultimo_mensaje = ok
                    ? "Consulta finalizada. Tratamiento registrado (ID: " + tratamiento.getId_tratamiento() + ")."
                    : "No se pudo finalizar la consulta.";

            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }

    public Tratamiento obtenerPorId(int id_tratamiento) {
        try {
            if (id_tratamiento <= 0) {
                ultimo_mensaje = "ID inválido.";
                return null;
            }
            return tratamientoDAO.obtenerPorId(id_tratamiento);
        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return null;
        }
    }

    public List<Tratamiento> obtenerTodos() {
        try {
            return tratamientoDAO.obtenerTodos();
        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return List.of();
        }
    }

    public boolean actualizar(Tratamiento tratamiento) {
        try {
            if (tratamiento == null) {
                ultimo_mensaje = "Tratamiento inválido.";
                return false;
            }
            if (tratamiento.getId_tratamiento() <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }
            if (tratamiento.getId_cita() <= 0) {
                ultimo_mensaje = "ID de cita inválido.";
                return false;
            }
            if (tratamiento.getDescripcion() == null || tratamiento.getDescripcion().trim().isEmpty()) {
                ultimo_mensaje = "Descripción obligatoria.";
                return false;
            }
            if (tratamiento.getDuracion() <= 0) {
                ultimo_mensaje = "Duración inválida.";
                return false;
            }

            tratamiento.setDescripcion(tratamiento.getDescripcion().trim());

            boolean ok = tratamientoDAO.actualizar(tratamiento);
            ultimo_mensaje = ok ? "Tratamiento actualizado." : "No se pudo actualizar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }

    public boolean eliminar(int id_tratamiento) {
        try {
            if (id_tratamiento <= 0) {
                ultimo_mensaje = "ID inválido.";
                return false;
            }

            boolean ok = tratamientoDAO.eliminar(id_tratamiento);
            ultimo_mensaje = ok ? "Tratamiento eliminado." : "No se pudo eliminar.";
            return ok;

        } catch (Exception e) {
            ultimo_mensaje = e.getMessage();
            return false;
        }
    }
}

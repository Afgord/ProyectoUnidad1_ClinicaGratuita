/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.sql.Date;
import java.util.List;
import model.Cita_Medica;

/**
 *
 * @author Afgord
 */
public interface ICita_MedicaDAO {

    boolean insertar(Cita_Medica cita);

    Cita_Medica obtenerPorId(int id_cita);

    List<Cita_Medica> obtenerCitasPorPaciente(int id_paciente);

    List<Cita_Medica> obtenerCitasPorFecha(Date fecha);

    List<Cita_Medica> obtenerTodos();

    boolean actualizar(Cita_Medica cita);

    boolean eliminar(int id_cita);

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import model.Agenda_Diaria;

/**
 *
 * @author Afgord
 */
public interface IAgenda_DiariaDAO {

    List<Agenda_Diaria> obtenerAgendaHoy();

    List<Agenda_Diaria> obtenerAgendaHoyPorDoctor(int idDoctor);

    // Método temporal para las pruebas con citas en fin de semana
    List<Agenda_Diaria> obtenerProximasCitasPorDoctor(int idDoctor);

}

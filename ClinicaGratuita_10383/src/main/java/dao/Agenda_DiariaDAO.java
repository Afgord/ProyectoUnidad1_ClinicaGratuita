/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.IAgenda_DiariaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Agenda_Diaria;

/**
 *
 * @author Afgord
 */
public class Agenda_DiariaDAO implements IAgenda_DiariaDAO {

    @Override
    public List<Agenda_Diaria> obtenerAgendaHoy() {

        String sql = "SELECT id_cita, hora, paciente, edad, motivo_consulta, estado "
                + "FROM vista_agenda_diaria "
                + "ORDER BY hora ASC";

        List<Agenda_Diaria> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Agenda_Diaria a = new Agenda_Diaria();
                a.setId_cita(rs.getInt("id_cita"));
                a.setHora(rs.getTime("hora"));
                a.setPaciente(rs.getString("paciente"));
                a.setEdad(rs.getInt("edad"));
                a.setMotivo_consulta(rs.getString("motivo_consulta"));
                a.setEstado(rs.getString("estado"));
                lista.add(a);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener agenda diaria: " + e.getMessage(), e);
        }

        return lista;

    }

}

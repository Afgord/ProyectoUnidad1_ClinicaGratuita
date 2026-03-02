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

    @Override
    public List<Agenda_Diaria> obtenerAgendaHoyPorDoctor(int idDoctor) {
        String sql = """
        SELECT
            c.id_cita,
            c.id_doctor,
            c.hora,
            p.nombre AS paciente,
            p.edad,
            c.motivo_consulta,
            c.estado
        FROM citas_medicas c
        JOIN pacientes p ON c.id_paciente = p.id_paciente
        WHERE c.id_doctor = ?
          AND c.estado IN ('programada','en curso')
          AND c.fecha >= CURDATE()
          AND (
                c.fecha > CURDATE()
                OR (c.fecha = CURDATE() AND c.hora >= SUBTIME(CURTIME(), '00:10:00'))
              )
        ORDER BY c.fecha ASC, c.hora ASC
    """;

        List<Agenda_Diaria> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDoctor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Agenda_Diaria a = new Agenda_Diaria();
                    a.setId_cita(rs.getInt("id_cita"));
                    a.setId_doctor(rs.getInt("id_doctor"));
                    a.setHora(rs.getTime("hora"));
                    a.setPaciente(rs.getString("paciente"));
                    a.setEdad(rs.getInt("edad"));
                    a.setMotivo_consulta(rs.getString("motivo_consulta"));
                    a.setEstado(rs.getString("estado"));
                    lista.add(a);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener agenda por doctor: " + e.getMessage(), e);
        }

        return lista;
    }

    // Esta es el método temporal para las pruebas con citas en fin de semana
    @Override
    public List<Agenda_Diaria> obtenerProximasCitasPorDoctor(int idDoctor) {
        // Próximas citas: desde hoy en adelante, solo L-V, estado programada,
        // y si es hoy, que sea desde (ahora - 10 min) para tolerancia.
        String sql = """
        SELECT
            c.id_cita,
            c.id_doctor,
            c.hora,
            p.nombre AS paciente,
            p.edad,
            c.motivo_consulta,
            c.estado
        FROM citas_medicas c
        JOIN pacientes p ON c.id_paciente = p.id_paciente
        WHERE c.id_doctor = ?
          AND c.estado = 'programada'
          AND WEEKDAY(c.fecha) < 5
          AND (
                c.fecha > CURDATE()
                OR (c.fecha = CURDATE() AND c.hora >= SUBTIME(CURTIME(), '00:10:00'))
              )
        ORDER BY c.fecha ASC, c.hora ASC
    """;

        List<Agenda_Diaria> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDoctor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Agenda_Diaria a = new Agenda_Diaria();
                    a.setId_cita(rs.getInt("id_cita"));
                    a.setId_doctor(rs.getInt("id_doctor"));
                    a.setHora(rs.getTime("hora"));
                    a.setPaciente(rs.getString("paciente"));
                    a.setEdad(rs.getInt("edad"));
                    a.setMotivo_consulta(rs.getString("motivo_consulta"));
                    a.setEstado(rs.getString("estado"));
                    lista.add(a);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener próximas citas: " + e.getMessage(), e);
        }

        return lista;
    }

}

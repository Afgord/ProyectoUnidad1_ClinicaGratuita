/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.ICita_MedicaDAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cita_Medica;

/**
 *
 * @author Afgord
 */
public class Cita_MedicaDAO implements ICita_MedicaDAO {

    @Override
    public boolean insertar(Cita_Medica cita) {
        String sql = "INSERT INTO citas_medicas (id_paciente, id_doctor, fecha, hora, motivo_consulta) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, cita.getId_paciente());
                ps.setInt(2, cita.getId_doctor());
                ps.setDate(3, cita.getFecha());
                ps.setTime(4, cita.getHora());
                ps.setString(5, cita.getMotivo_consulta());

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar cita: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cita_Medica obtenerPorId(int id_cita) {
        String sql = "SELECT * FROM citas_medicas WHERE id_cita = ?";
        Cita_Medica cita = null;

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id_cita);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        cita = new Cita_Medica();
                        cita.setId_cita(rs.getInt("id_cita"));
                        cita.setId_paciente(rs.getInt("id_paciente"));
                        cita.setId_doctor(rs.getInt("id_doctor"));
                        cita.setFecha(rs.getDate("fecha"));
                        cita.setHora(rs.getTime("hora"));
                        cita.setMotivo_Consulta(rs.getString("motivo_consulta"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cita por ID: " + e.getMessage());
        }
        return cita;
    }

    @Override
    public List<Cita_Medica> obtenerCitasPorPaciente(int id_paciente) {
        String sql = "SELECT * FROM citas_medicas WHERE id_paciente = ? "
                + "ORDER BY fecha DESC, hora DESC";
        List<Cita_Medica> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id_paciente);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Cita_Medica cita = new Cita_Medica();
                        cita.setId_cita(rs.getInt("id_cita"));
                        cita.setId_paciente(rs.getInt("id_paciente"));
                        cita.setId_doctor(rs.getInt("id_doctor"));
                        cita.setFecha(rs.getDate("fecha"));
                        cita.setHora(rs.getTime("hora"));
                        cita.setMotivo_Consulta(rs.getString("motivo_consulta"));

                        lista.add(cita);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener citas del paciente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Cita_Medica> obtenerCitasPorFecha(Date fecha) {
        String sql = "SELECT * FROM citas_medicas WHERE fecha = ? ORDER BY hora ASC";
        List<Cita_Medica> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, fecha);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Cita_Medica cita = new Cita_Medica();
                        cita.setId_cita(rs.getInt("id_cita"));
                        cita.setId_paciente(rs.getInt("id_paciente"));
                        cita.setId_doctor(rs.getInt("id_doctor"));
                        cita.setFecha(rs.getDate("fecha"));
                        cita.setHora(rs.getTime("hora"));
                        cita.setMotivo_Consulta(rs.getString("motivo_consulta"));

                        lista.add(cita);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar por fecha: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Cita_Medica> obtenerTodos() {
        String sql = "SELECT * FROM citas_medicas ORDER BY fecha DESC, hora DESC";
        List<Cita_Medica> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Cita_Medica cita = new Cita_Medica();
                        cita.setId_cita(rs.getInt("id_cita"));
                        cita.setId_paciente(rs.getInt("id_paciente"));
                        cita.setId_doctor(rs.getInt("id_doctor"));
                        cita.setFecha(rs.getDate("fecha"));
                        cita.setHora(rs.getTime("hora"));
                        cita.setMotivo_Consulta(rs.getString("motivo_consulta"));

                        lista.add(cita);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todas las citas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Cita_Medica cita) {
        String sql = "UPDATE citas_medicas SET id_paciente = ?, id_doctor = ?, fecha = ?, "
                + " hora = ?, motivo_consulta = ? WHERE id_cita = ?";

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, cita.getId_paciente());
                ps.setInt(2, cita.getId_doctor());
                ps.setDate(3, cita.getFecha());
                ps.setTime(4, cita.getHora());
                ps.setString(5, cita.getMotivo_consulta());
                ps.setInt(6, cita.getId_cita());

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar cita: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id_cita) {
        String sql = "DELETE FROM citas_medicas WHERE id_cita = ?";

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id_cita);
                return ps.executeUpdate() > 0;

            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar la cita: " + e.getMessage());
            return false;
        }
    }

}

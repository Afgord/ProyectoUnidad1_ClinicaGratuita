/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.ICita_MedicaDAO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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
        String sql = "{CALL sp_agendar_cita(?, ?, ?, ?, ?)}";
        try (Connection con = ConexionDB.getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.setInt(1, cita.getId_paciente());
                cs.setInt(2, cita.getId_doctor());
                cs.setDate(3, cita.getFecha());
                cs.setTime(4, cita.getHora());
                cs.setString(5, cita.getMotivo_Consulta());
                cs.execute();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al agendar cita: " + e.getMessage(), e);
        }
    }

    @Override
    public Cita_Medica obtenerPorId(int id_cita) {
        String sql = "SELECT id_cita, id_paciente, id_doctor, fecha, hora, motivo_consulta, estado FROM citas_medicas WHERE id_cita = ?";
        Cita_Medica cita = null;
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
                    cita.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener cita por ID: " + e.getMessage(), e);
        }
        return cita;
    }

    @Override
    public List<Cita_Medica> obtenerCitasPorPaciente(int id_paciente) {
        String sql = "SELECT id_cita, id_paciente, id_doctor, fecha, hora, motivo_consulta, estado FROM citas_medicas WHERE id_paciente = ? "
                + "ORDER BY fecha DESC, hora DESC";
        List<Cita_Medica> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
                    cita.setEstado(rs.getString("estado"));
                    lista.add(cita);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener cita por paciente: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Cita_Medica> obtenerCitasPorFecha(Date fecha) {
        String sql = "SELECT id_cita, id_paciente, id_doctor, fecha, hora, motivo_consulta, estado FROM citas_medicas WHERE fecha = ? ORDER BY hora ASC";
        List<Cita_Medica> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
                    cita.setEstado(rs.getString("estado"));
                    lista.add(cita);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener cita por fecha: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean existeCitaActiva(int idDoctor, Date fecha, Time hora) {
        String sql = """
        SELECT 1
        FROM citas_medicas
        WHERE id_doctor = ?
          AND fecha = ?
          AND hora = ?
          AND estado IN ('programada','en curso')
        LIMIT 1
    """;

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDoctor);
            ps.setDate(2, fecha);
            ps.setTime(3, hora);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar disponibilidad: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Time> obtenerHorasOcupadas(int idDoctor, Date fecha) {
        String sql = """
        SELECT hora
        FROM citas_medicas
        WHERE id_doctor = ?
          AND fecha = ?
          AND estado IN ('programada','en curso')
    """;

        List<Time> horas = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDoctor);
            ps.setDate(2, fecha);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    horas.add(rs.getTime("hora"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener horas ocupadas: " + e.getMessage(), e);
        }

        return horas;
    }

    @Override
    public List<Cita_Medica> obtenerTodos() {
        String sql = "SELECT id_cita, id_paciente, id_doctor, fecha, hora, motivo_consulta, estado FROM citas_medicas ORDER BY fecha DESC, hora DESC";
        List<Cita_Medica> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cita_Medica cita = new Cita_Medica();
                cita.setId_cita(rs.getInt("id_cita"));
                cita.setId_paciente(rs.getInt("id_paciente"));
                cita.setId_doctor(rs.getInt("id_doctor"));
                cita.setFecha(rs.getDate("fecha"));
                cita.setHora(rs.getTime("hora"));
                cita.setMotivo_Consulta(rs.getString("motivo_consulta"));
                cita.setEstado(rs.getString("estado"));
                lista.add(cita);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todas las citas: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean actualizar(Cita_Medica cita) {
        String sql = "UPDATE citas_medicas SET id_paciente = ?, id_doctor = ?, fecha = ?, "
                + " hora = ?, motivo_consulta = ?, estado = ? WHERE id_cita = ?";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cita.getId_paciente());
            ps.setInt(2, cita.getId_doctor());
            ps.setDate(3, cita.getFecha());
            ps.setTime(4, cita.getHora());
            ps.setString(5, cita.getMotivo_Consulta());
            ps.setString(6, cita.getEstado());
            ps.setInt(7, cita.getId_cita());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cita: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id_cita) {
        String sql = "DELETE FROM citas_medicas WHERE id_cita = ?";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_cita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cita: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarEstado(int idCita, String nuevoEstado) {
        String sql = "UPDATE citas_medicas SET estado = ? WHERE id_cita = ?";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado: " + e.getMessage(), e);
        }
    }

    @Override
    public String obtenerEstado(int idCita) {
        String sql = "SELECT estado FROM citas_medicas WHERE id_cita = ?";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("estado");
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener estado: " + e.getMessage(), e);
        }
    }

}

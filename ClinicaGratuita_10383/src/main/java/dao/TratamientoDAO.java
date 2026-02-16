/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.ITratamientoDAO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Tratamiento;

/**
 *
 * @author Afgord
 */
public class TratamientoDAO implements ITratamientoDAO {

    @Override
    public boolean insertar(Tratamiento tratamiento) {
        String sql = "{call sp_finalizar_consulta(?, ?, ?)}";
        try (Connection con = ConexionDB.getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.setInt(1, tratamiento.getId_cita());
                cs.setString(2, tratamiento.getDescripcion());
                cs.setInt(3, tratamiento.getDuracion());
                return cs.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el tratamiento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Tratamiento obtenerPorId(int id_tratamiento) {
        String sql = "SELECT id_tratamiento, id_cita, descripcion, "
                + "duracion FROM tratamientos WHERE id_tratamiento = ?";
        Tratamiento tratamiento = null;

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_tratamiento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tratamiento = new Tratamiento();
                    tratamiento.setId_tratamiento(rs.getInt("id_tratamiento"));
                    tratamiento.setId_cita(rs.getInt("id_cita"));
                    tratamiento.setDescripcion(rs.getString("descripcion"));
                    tratamiento.setDuracion(rs.getInt("duracion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tratamiento por ID: " + e.getMessage());
        }
        return tratamiento;
    }

    @Override
    public List<Tratamiento> obtenerTodos() {
        String sql = "SELECT id_tratamiento, id_cita, descripcion, duracion FROM tratamientos";
        List<Tratamiento> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tratamiento tratamiento = new Tratamiento();
                tratamiento.setId_tratamiento(rs.getInt("id_tratamiento"));
                tratamiento.setId_cita(rs.getInt("id_cita"));
                tratamiento.setDescripcion(rs.getString("descripcion"));
                tratamiento.setDuracion(rs.getInt("duracion"));

                lista.add(tratamiento);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tratamientos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Tratamiento tratamiento) {
        String sql = "UPDATE tratamientos SET id_cita = ?, descripcion = ?, duracion = ? "
                + "WHERE id_tratamiento = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tratamiento.getId_cita());
            ps.setString(2, tratamiento.getDescripcion());
            ps.setInt(3, tratamiento.getDuracion());
            ps.setInt(4, tratamiento.getId_tratamiento());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el tratamiento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id_tratamiento) {
        String sql = "DELETE FROM tratamientos WHERE id_tratamiento = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_tratamiento);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar el tratamiento: " + e.getMessage());
            return false;
        }
    }
}

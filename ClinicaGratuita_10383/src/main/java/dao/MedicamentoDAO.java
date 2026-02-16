/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.IMedicamentoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Medicamento;

/**
 *
 * @author Afgord
 */
public class MedicamentoDAO implements IMedicamentoDAO {

    @Override
    public boolean insertar(Medicamento medicamento) {
        String sql = "INSERT INTO medicamentos (nombre) VALUES (?)";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, medicamento.getNombre());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar medicamento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Medicamento obtenerPorId(int id_medicamento) {
        String sql = "SELECT id_medicamento, nombre FROM medicamentos WHERE id_medicamento = ?";
        Medicamento medicamento = null;
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_medicamento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    medicamento = new Medicamento();
                    medicamento.setId_medicamento(rs.getInt("id_medicamento"));
                    medicamento.setNombre(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener medicamento por ID: " + e.getMessage());
        }
        return medicamento;
    }

    @Override
    public List<Medicamento> obtenerTodos() {
        String sql = "SELECT id_medicamento, nombre FROM medicamentos";
        List<Medicamento> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medicamento medicamento = new Medicamento();
                medicamento.setId_medicamento(rs.getInt("id_medicamento"));
                medicamento.setNombre(rs.getString("nombre"));
                lista.add(medicamento);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar medicamentos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Medicamento medicamento) {
        String sql = "UPDATE medicamentos SET nombre = ? WHERE id_medicamento = ?";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, medicamento.getNombre());
            ps.setInt(2, medicamento.getId_medicamento());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar medicamento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id_medicamento) {
        String sql = "DELETE FROM medicamentos WHERE id_medicamento = ?";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_medicamento);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar medicamento: " + e.getMessage());
            return false;
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.ITratamiento_MedicamentoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Tratamiento_Medicamento;

/**
 *
 * @author Afgord
 */
public class Tratamiento_MedicamentoDAO implements ITratamiento_MedicamentoDAO {

    @Override
    public boolean insertar(Tratamiento_Medicamento relacion) {
        String sql = "INSERT INTO tratamientos_medicamentos (id_tratamiento, id_medicamento, indicaciones) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, relacion.getId_tratamiento());
            ps.setInt(2, relacion.getId_medicamento());
            ps.setString(3, relacion.getIndicaciones());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al asignar medicamento al tratamiento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Tratamiento_Medicamento> obtenerPorTratamiento(int id_tratamiento) {
        String sql = "SELECT id_tratamiento, id_medicamento, indicaciones FROM tratamientos_medicamentos "
                + "WHERE id_tratamiento = ?";
        List<Tratamiento_Medicamento> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_tratamiento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tratamiento_Medicamento tm = new Tratamiento_Medicamento();
                    tm.setId_tratamiento(rs.getInt("id_tratamiento"));
                    tm.setId_medicamento(rs.getInt("id_medicamento"));
                    tm.setIndicaciones(rs.getString("indicaciones"));
                    lista.add(tm);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener medicamentos del tratamiento: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Tratamiento_Medicamento> obtenerPorMedicamento(int id_medicamento) {
        String sql = "SELECT id_tratamiento, id_medicamento, indicaciones FROM tratamientos_medicamentos "
                + "WHERE id_medicamento = ?";
        List<Tratamiento_Medicamento> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_medicamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tratamiento_Medicamento tm = new Tratamiento_Medicamento();
                    tm.setId_tratamiento(rs.getInt("id_tratamiento"));
                    tm.setId_medicamento(rs.getInt("id_medicamento"));
                    tm.setIndicaciones(rs.getString("indicaciones"));
                    lista.add(tm);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tratamientos por medicamento: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean eliminar(int id_tratamiento, int id_medicamento) {
        String sql = "DELETE FROM tratamientos_medicamentos WHERE id_tratamiento = ? AND id_medicamento = ?";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_tratamiento);
            ps.setInt(2, id_medicamento);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar relación tratamiento-medicamento: " + e.getMessage());
            return false;
        }
    }

}

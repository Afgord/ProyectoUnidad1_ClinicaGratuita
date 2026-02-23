/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.ITratamiento_MedicamentoDAO;
import java.sql.CallableStatement;
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
        // Usa tu SP existente en la BD
        String sql = "{CALL sp_tratamiento_medicamento(?, ?, ?)}";

        try (Connection con = ConexionDB.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, relacion.getId_tratamiento());
            cs.setInt(2, relacion.getId_medicamento());
            cs.setString(3, relacion.getIndicaciones());

            cs.execute();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Error al asignar medicamento al tratamiento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Tratamiento_Medicamento> obtenerPorTratamiento(int id_tratamiento) {
        String sql = "SELECT id_tratamiento, id_medicamento, indicaciones "
                + "FROM tratamientos_medicamentos "
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

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener medicamentos del tratamiento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Tratamiento_Medicamento> obtenerPorMedicamento(int id_medicamento) {
        String sql = "SELECT id_tratamiento, id_medicamento, indicaciones "
                + "FROM tratamientos_medicamentos "
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

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener tratamientos por medicamento: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id_tratamiento, int id_medicamento) {
        String sql = "DELETE FROM tratamientos_medicamentos "
                + "WHERE id_tratamiento = ? AND id_medicamento = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_tratamiento);
            ps.setInt(2, id_medicamento);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar relación tratamiento-medicamento: " + e.getMessage(), e);
        }
    }

}

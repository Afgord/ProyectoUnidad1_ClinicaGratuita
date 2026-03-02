/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.IRecetaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Receta;

/**
 *
 * @author Afgord
 */
public class RecetaDAO implements IRecetaDAO {

    @Override
    public List<Receta> obtenerPorCita(int id_cita) {
        String sql = "SELECT id_cita, paciente, doctor, medicamento, indicaciones, dias_tratamiento "
                + "FROM vista_receta_completa WHERE id_cita = ? ORDER BY medicamento ASC";

        List<Receta> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_cita);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Receta r = new Receta();
                    r.setId_cita(rs.getInt("id_cita"));
                    r.setPaciente(rs.getString("paciente"));
                    r.setDoctor(rs.getString("doctor"));
                    r.setMedicamento(rs.getString("medicamento"));
                    r.setIndicaciones(rs.getString("indicaciones"));
                    r.setDias_tratamiento(rs.getInt("dias_tratamiento"));
                    lista.add(r);
                }
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener receta: " + e.getMessage(), e);
        }
    }

}

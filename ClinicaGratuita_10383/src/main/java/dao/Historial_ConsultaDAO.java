/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.IHistorial_ConsultaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Historial_Consulta;

/**
 *
 * @author Afgord
 */
public class Historial_ConsultaDAO implements IHistorial_ConsultaDAO {

    @Override
    public List<Historial_Consulta> obtenerHistorialConsultas(int idPaciente) {
        String sql = """
            SELECT fecha, motivo_consulta, diagnostico
            FROM vista_historial_paciente
            WHERE id_paciente = ?
            ORDER BY fecha DESC
        """;

        List<Historial_Consulta> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Historial_Consulta historial = new Historial_Consulta();
                    historial.setFecha(rs.getDate("fecha"));
                    historial.setMotivo(rs.getString("motivo_consulta"));
                    historial.setDiagnostico(rs.getString("diagnostico"));
                    lista.add(historial);
                }
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener historial: " + e.getMessage(), e);
        }
    }

}

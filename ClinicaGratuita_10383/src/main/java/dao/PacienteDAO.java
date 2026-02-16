/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.IPacienteDAO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Paciente;

/**
 *
 * @author Afgord
 */
public class PacienteDAO implements IPacienteDAO {

    @Override
    public boolean insertar(Paciente paciente) {
        String sql = "{call sp_registrar_paciente(?, ?, ?, ?, ?, ?)}";
        try (Connection con = ConexionDB.getConnection()) {
            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.setString(1, paciente.getNombre());
                cs.setInt(2, paciente.getEdad());
                cs.setString(3, paciente.getSexo());
                cs.setString(4, paciente.getDireccion());
                cs.setString(5, paciente.getEmail());
                cs.setString(6, paciente.getTelefono());

                cs.execute();
                return true;

            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el paciente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Paciente obtenerPorId(int id_paciente) {
        String sql = "SELECT * FROM pacientes WHERE id_paciente = ?";
        Paciente paciente = null;

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id_paciente);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    paciente = new Paciente();
                    paciente.setId_paciente(rs.getInt("id_paciente"));
                    paciente.setNombre(rs.getString("nombre"));
                    paciente.setEdad(rs.getInt("edad"));
                    paciente.setSexo(rs.getString("sexo"));
                    paciente.setDireccion(rs.getString("direccion"));
                    paciente.setEmail(rs.getString("email"));
                    paciente.setTelefono(rs.getString("telefono"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el paciente por ID: " + e.getMessage());
        }

        return paciente;
    }

    @Override
    public List<Paciente> obtenerTodos() {
        String sql = "SELECT * FROM pacientes";
        List<Paciente> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Paciente paciente = new Paciente();
                        paciente.setId_paciente(rs.getInt("id_paciente"));
                        paciente.setNombre(rs.getString("nombre"));
                        paciente.setEdad(rs.getInt("edad"));
                        paciente.setSexo(rs.getString("sexo"));
                        paciente.setDireccion(rs.getString("direccion"));
                        paciente.setEmail(rs.getString("email"));
                        paciente.setTelefono(rs.getString("telefono"));
                        lista.add(paciente);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener lista de pacientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Paciente paciente) {
        String sql = "UPDATE pacientes SET nombre = ?, edad = ?, sexo = ?, "
                + "direccion = ?, email = ?, telefono = ? WHERE id_paciente = ?";

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, paciente.getNombre());
                ps.setInt(2, paciente.getEdad());
                ps.setString(3, paciente.getSexo());
                ps.setString(4, paciente.getDireccion());
                ps.setString(5, paciente.getEmail());
                ps.setString(6, paciente.getTelefono());
                ps.setInt(7, paciente.getId_paciente());

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el paciente " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id_paciente) {
        String sql = "DELETE FROM pacientes WHERE id_paciente = ?";
        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id_paciente);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el paciente: " + e.getMessage());
            return false;
        }
    }

}

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
        try (Connection con = ConexionDB.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, paciente.getNombre());
            cs.setInt(2, paciente.getEdad());
            cs.setString(3, paciente.getSexo());
            cs.setString(4, paciente.getDireccion());
            cs.setString(5, paciente.getEmail());
            cs.setString(6, paciente.getTelefono());

            cs.execute();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Paciente obtenerPorId(int id_paciente) {
        String sql = "SELECT id_paciente, nombre, edad, sexo, direccion, email, telefono "
                + "FROM pacientes WHERE id_paciente = ?";
        Paciente paciente = null;

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_paciente);

            try (ResultSet rs = ps.executeQuery()) {
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
            throw new RuntimeException(e.getMessage(), e);
        }
        return paciente;
    }

    @Override
    public List<Paciente> obtenerTodos() {
        String sql = "SELECT id_paciente, nombre, edad, sexo, direccion, email, telefono"
                + " FROM pacientes";
        List<Paciente> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

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

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean actualizar(Paciente paciente) {
        String sql = "{call sp_actualizar_paciente(?, ?, ?, ?)}";

        try (Connection con = ConexionDB.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, paciente.getId_paciente());
            cs.setString(2, paciente.getDireccion());
            cs.setString(3, paciente.getTelefono());
            cs.setString(4, paciente.getEmail());

            cs.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id_paciente) {
        String sql = "DELETE FROM pacientes WHERE id_paciente = ?";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_paciente);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

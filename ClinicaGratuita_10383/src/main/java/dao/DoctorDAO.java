package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.IDoctorDAO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Doctor;

/**
 *
 * @author Afgord
 */
public class DoctorDAO implements IDoctorDAO {

    @Override
    public boolean insertar(Doctor doctor) {
        String sql = "{CALL sp_registrar_doctor(?, ?, ?, ?)}";
        try (Connection con = ConexionDB.getConnection(); CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, doctor.getNombre());
            cs.setString(2, doctor.getEmail());
            cs.setString(3, doctor.getTelefono());
            cs.setString(4, doctor.getEspecialidad());
            cs.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public Doctor obtenerPorId(int id_doctor) {
        String sql = "SELECT id_doctor, nombre, email, telefono, especialidad FROM doctores WHERE id_doctor = ?";
        Doctor doctor = null;
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, id_doctor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    doctor = new Doctor();
                    doctor.setId_doctor(rs.getInt("id_doctor"));
                    doctor.setNombre(rs.getString("nombre"));
                    doctor.setEmail(rs.getString("email"));
                    doctor.setTelefono(rs.getString("telefono"));
                    doctor.setEspecialidad(rs.getString("especialidad"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el doctor: " + e.getMessage(), e);
        }
        return doctor;
    }

    @Override
    public List<Doctor> obtenerTodos() {
        String sql = "SELECT id_doctor, nombre, email, telefono, especialidad "
                + "FROM doctores ORDER BY nombre ASC";
        List<Doctor> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId_doctor(rs.getInt("id_doctor"));
                doctor.setNombre(rs.getString("nombre"));
                doctor.setEmail(rs.getString("email"));
                doctor.setTelefono(rs.getString("telefono"));
                doctor.setEspecialidad(rs.getString("especialidad"));
                lista.add(doctor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de doctores: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Doctor> buscar(String criterio) {
        String sql = "SELECT id_doctor, nombre, email, telefono, especialidad "
                + "FROM doctores WHERE nombre LIKE ? OR especialidad LIKE ?";
        List<Doctor> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String busqueda = "%" + criterio + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Doctor doctor = new Doctor();
                    doctor.setId_doctor(rs.getInt("id_doctor"));
                    doctor.setNombre(rs.getString("nombre"));
                    doctor.setEmail(rs.getString("email"));
                    doctor.setTelefono(rs.getString("telefono"));
                    doctor.setEspecialidad(rs.getString("especialidad"));
                    lista.add(doctor);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar doctores: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<String> obtenerEspecialidadesUnicas() {
        String sql = "SELECT DISTINCT especialidad FROM doctores ORDER BY especialidad ASC";
        List<String> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("especialidad"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener especialidades: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean actualizar(Doctor doctor) {
        String sql = "{CALL sp_actualizar_doctor(?, ?, ?, ?, ?)}";
        try (Connection con = ConexionDB.getConnection(); CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, doctor.getId_doctor());
            cs.setString(2, doctor.getNombre());
            cs.setString(3, doctor.getEmail());
            cs.setString(4, doctor.getTelefono());
            cs.setString(5, doctor.getEspecialidad());
            cs.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar al doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id_doctor) {
        String sql = "DELETE FROM doctores WHERE id_doctor = ?";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_doctor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar al doctor: " + e.getMessage(), e);
        }
    }
}

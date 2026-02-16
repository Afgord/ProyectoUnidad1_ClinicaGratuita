package dao;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import interfaces.IDoctorDAO;
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
        String sql = "INSERT INTO doctores (nombre, email, telefono, especialidad) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, doctor.getNombre());
                ps.setString(2, doctor.getEmail());
                ps.setString(3, doctor.getTelefono());
                ps.setString(4, doctor.getEspecialidad());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar doctor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Doctor obtenerPorId(int id_doctor) {
        String sql = "SELECT * FROM doctores WHERE id_doctor = ?";
        Doctor doctor = null;
        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener doctor por ID: " + e.getMessage());
        }
        return doctor;
    }

    @Override
    public List<Doctor> obtenerTodos() {
        String sql = "SELECT * FROM doctores ORDER BY nombre ASC";
        List<Doctor> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los doctores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Doctor> buscar(String criterio) {
        String sql = "SELECT * FROM doctores WHERE nombre LIKE ? OR especialidad LIKE ?";
        List<Doctor> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar doctores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<String> obtenerEspecialidadesUnicas() {
        String sql = "SELECT DISTINCT especialidad FROM doctores ORDER BY especialidad ASC";
        List<String> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(rs.getString("especialidad"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener especialidades únicas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Doctor doctor) {
        String sql = "UPDATE doctores SET nombre = ?, email = ?, telefono = ?, "
                + "especialidad = ? WHERE id_doctor = ?";
        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, doctor.getNombre());
                ps.setString(2, doctor.getEmail());
                ps.setString(3, doctor.getTelefono());
                ps.setString(4, doctor.getEspecialidad());
                ps.setInt(5, doctor.getId_doctor());

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar doctor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id_doctor) {
        String sql = "DELETE FROM doctores WHERE id_doctor = ?";

        try (Connection con = ConexionDB.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id_doctor);

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar doctor: " + e.getMessage());
            return false;
        }
    }
}

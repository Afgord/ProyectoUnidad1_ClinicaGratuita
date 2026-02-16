/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import model.Doctor;

/**
 *
 * @author Afgord
 */
public interface IDoctorDAO {

    boolean insertar(Doctor doctor);

    Doctor obtenerPorId(int id_doctor);

    List<Doctor> obtenerTodos();

    List<Doctor> buscar(String criterio);

    List<String> obtenerEspecialidadesUnicas();

    boolean actualizar(Doctor doctor);

    boolean eliminar(int id_doctor);

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import model.Paciente;

/**
 *
 * @author Afgord
 */
public interface IPacienteDAO {

    boolean insertar(Paciente paciente);

    Paciente obtenerPorId(int id_paciente);

    List<Paciente> obtenerTodos();

    boolean actualizar(Paciente paciente);

    boolean eliminar(int id_paciente);

}

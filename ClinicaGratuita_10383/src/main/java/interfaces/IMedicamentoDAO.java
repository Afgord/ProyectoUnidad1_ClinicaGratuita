/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import model.Medicamento;

/**
 *
 * @author Afgord
 */
public interface IMedicamentoDAO {

    boolean insertar(Medicamento medicamento);

    Medicamento obtenerPorId(int id_medicamento);

    List<Medicamento> obtenerTodos();

    boolean actualizar(Medicamento medicamento);

    boolean eliminar(int id_medicamento);

}

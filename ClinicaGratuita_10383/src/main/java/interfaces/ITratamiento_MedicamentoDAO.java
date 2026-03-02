/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import model.Tratamiento_Medicamento;

/**
 *
 * @author Afgord
 */
public interface ITratamiento_MedicamentoDAO {

    boolean insertar(Tratamiento_Medicamento relacion);

    List<Tratamiento_Medicamento> obtenerPorTratamiento(int id_tratamiento);

    List<Tratamiento_Medicamento> obtenerPorMedicamento(int id_medicamento);

    boolean eliminar(int id_tratamiento, int id_medicamento);

}

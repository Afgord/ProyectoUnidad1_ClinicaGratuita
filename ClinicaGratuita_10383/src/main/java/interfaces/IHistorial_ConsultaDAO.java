/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import model.Historial_Consulta;

/**
 *
 * @author Afgord
 */
public interface IHistorial_ConsultaDAO {

    List<Historial_Consulta> obtenerHistorialConsultas(int idPaciente);

}

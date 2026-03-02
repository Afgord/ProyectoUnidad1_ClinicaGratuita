/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import model.Receta;

/**
 *
 * @author Afgord
 */
public interface IRecetaDAO {

    List<Receta> obtenerPorCita(int id_cita);

}

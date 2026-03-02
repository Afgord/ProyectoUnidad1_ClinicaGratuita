/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import model.Tratamiento;

/**
 *
 * @author Afgord
 */
public interface ITratamientoDAO {

    boolean insertar(Tratamiento tratamiento);

    Tratamiento obtenerPorId(int id_tratamiento);

    List<Tratamiento> obtenerTodos();

    boolean actualizar(Tratamiento tratamiento);

    boolean eliminar(int id_tratamiento);

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.clinicagratuita_10383;

import com.mycompany.clinicagratuita_10383.config.ConexionDB;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Afgord
 */
public class ClinicaGratuita_10383 {

  public static void main(String[] args) {
    try (Connection con = ConexionDB.getConnection()) {
      System.out.println("Conexión establecida con éxito: " + con);
    } catch (SQLException e) {
      System.err.println("Error de conexion: " + e.getMessage());
    }
  }
}

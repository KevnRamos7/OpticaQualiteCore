/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;

import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Cliente;
import com.redata.oq.model.Empleado;
import com.redata.oq.model.ExamenVista;
import com.redata.oq.model.Graduacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ControllerGraduacion {
             public List<Graduacion> getAll(String filtro, int idGraduacion) throws Exception
    {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM graduacion WHERE idGraduacion = ?";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        pstmt.setInt(1, idGraduacion);
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Graduacion> graduacion = new ArrayList<>();
        
        while (rs.next())
            graduacion.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return graduacion;
    }
    
private Graduacion fill(ResultSet rs) throws Exception
{
    Graduacion g = new Graduacion();
   
        g.setIdGraduacion(rs.getInt("idGraduacion"));
        g.setEsferaod(rs.getDouble("esferaod"));
        g.setEsferaoi(rs.getDouble("esferaoi"));
        g.setCilindrood(rs.getInt("cilindrood"));
        g.setCilindrooi(rs.getInt("cilindrooi"));
        g.setEjeoi(rs.getInt("ejeoi"));
        g.setEjeod(rs.getInt("ejeod"));
        g.setDip(rs.getString("dip"));

    
    
    return g;
}
}

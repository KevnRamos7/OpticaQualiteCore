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
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ControllerExamenVista {
         public List<ExamenVista> getAll(String filtro, int idCliente) throws Exception
    {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM examen_vista WHERE idCliente = ?";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        pstmt.setInt(1, idCliente);
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<ExamenVista> examenVista = new ArrayList<>();
        
        while (rs.next())
            examenVista.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return examenVista;
    }
    
private ExamenVista fill(ResultSet rs) throws Exception
{
    ExamenVista ex = new ExamenVista();
    Empleado e = new Empleado();
    Cliente c = new Cliente();
    Graduacion g = new Graduacion();
   

        ex.setIdExamenVista(rs.getInt("idExamenVista"));
        ex.setClave(rs.getString("clave"));
        ex.setFecha(rs.getString("fecha"));
        e.setIdEmpleado(rs.getInt("idEmpleado"));      
        c.setIdCliente(rs.getInt("idCliente"));   
        g.setIdGraduacion(rs.getInt("idGraduacion"));
        ex.setEmpleado(e);
        ex.setCliente(c);
        ex.setGraduacion(g);
    
    
    return ex;
}
}

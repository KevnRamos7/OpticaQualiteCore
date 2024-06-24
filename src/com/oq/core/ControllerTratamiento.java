/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;

import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Producto;
import com.redata.oq.model.Tratamiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ControllerTratamiento {
    public List <Tratamiento> getAll(String filtro) throws Exception
    {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM tratamiento";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Tratamiento> material = new ArrayList<>();
        
        while (rs.next())
            material.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return material;
    }
    
    private Tratamiento fill(ResultSet rs) throws Exception
    {
        Tratamiento tr = new Tratamiento();
       
        tr.setIdTratamiento(rs.getInt("idTratamiento"));
        tr.setNombre(rs.getString("nombre"));
        tr.setPrecioCompra(rs.getDouble("precioCompra"));
        tr.setPrecioVenta(rs.getDouble("precioVenta"));
        tr.setEstatus(rs.getInt("estatus"));
        
        return tr;
    }
}

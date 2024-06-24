/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;

import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Material;
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
public class ControllerMaterial {
        public List <Material> getAll(String filtro) throws Exception
    {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM material";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Material> material = new ArrayList<>();
        
        while (rs.next())
            material.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return material;
    }
    
    private Material fill(ResultSet rs) throws Exception
    {
        Material ma = new Material();
       
        ma.setIdMaterial(rs.getInt("idMaterial"));
        ma.setNombre(rs.getString("nombre"));
        ma.setPrecioCompra(rs.getDouble("precioCompra"));
        ma.setPrecioVenta(rs.getDouble("precioVenta"));
        
        return ma;
    }
}

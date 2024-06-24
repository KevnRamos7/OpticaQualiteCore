/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;

import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Mica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ControllerMica {
             public List<Mica> getAll(String filtro) throws Exception
    {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM tipo_mica";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql

        ResultSet rs = pstmt.executeQuery();
        
        List<Mica> mica = new ArrayList<>();
        
        while (rs.next())
            mica.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return mica;
    }
    
private Mica fill(ResultSet rs) throws Exception
{
    Mica mica = new Mica();
   

       mica.setIdTipoMica(rs.getInt("idTipoMica"));
       mica.setNombre(rs.getString("nombre"));
       mica.setPrecioCompra(rs.getDouble("precioCompra"));
       mica.setPrecioVenta(rs.getDouble("precioVenta"));
    
    return mica;
}
}

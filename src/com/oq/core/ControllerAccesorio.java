/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;
import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Accesorio;
import com.redata.oq.model.Empleado;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import com.redata.oq.model.Persona;
import com.redata.oq.model.Usuario;

import com.redata.oq.model.Producto;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kevin
 */
public class ControllerAccesorio {
    
   
    
    public boolean validarToken(String lastToken) throws SQLException, Exception{
        
        boolean r = false;
        String query = "SELECT * FROM v_empleados WHERE lastToken = ?"; 
        ConexionMySQL objConn = new ConexionMySQL();
        Connection conn = objConn.open();
        PreparedStatement pstm = conn.prepareStatement(query);
        pstm.setString(1, lastToken);
        ResultSet rs = pstm.executeQuery();
         
        if(rs.next()){
            r = true;
        }
        
        
        
        pstm.close();
        conn.close();
        objConn.close();
        
        return r;
    }
    
    

    public int insert(Accesorio a, String lastToken) throws Exception { //necesita como parametro un objeto tipo empleado para agregar a la bd
        //Definimos la consulta SQL que invoca al Stored Procedure:
        //Definimos la cadena String que contendra la estructura del Stored Procedure es decir la consulta sql
        
     
            
        
        if (validarToken(lastToken) == true){
        String sql = "{call insertarAccesorio(?, ?, ?, ?, ?, ?"
               
                + ", ?, ?, ?)}";  // Valores de Retorno


        
        //Aquí guardaremos los ID's que se generarán:
        //Definimos las variables que guardaran posteriormente los id y le asignamos un valor fuera de rango
        int idProductoGenerado = -1;
        int idAccesorioGenerado = -1;
        String codigoBarrasGenerado = "";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        //Creamos un callableStatement pasandole la llamada 
        //Para ejecutar instrucciones NO SQL
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los valores de los parámetros de los datos personales 
        //en el orden en que los pide el procedimiento almacenado, 
        //comenzando en 1:
        //Establecemos los datos en el mismo orden que tenemos en la base de datos
        cstmt.setString(1, a.getProducto().getCodigoBarras());
        cstmt.setString(2, a.getProducto().getNombre());
        cstmt.setString(3, a.getProducto().getMarca());
        cstmt.setDouble(4, a.getProducto().getPrecioCompra());
        cstmt.setDouble(5, a.getProducto().getPrecioVenta());
        cstmt.setInt(6, a.getProducto().getExistencias());
       
        

        //Registramos los parámetros de salida:
        //Types se importa de sql y se le define el tipo de datos de sql
        cstmt.registerOutParameter(7, Types.INTEGER);
        cstmt.registerOutParameter(8, Types.INTEGER);
        cstmt.registerOutParameter(9, Types.VARCHAR);
        

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idProductoGenerado = cstmt.getInt(7);
        idAccesorioGenerado = cstmt.getInt(8);
        codigoBarrasGenerado = cstmt.getString(9);
       

        a.setIdAccesorio(idAccesorioGenerado);
        a.getProducto().setIdProducto(idProductoGenerado);
        a.getProducto().setCodigoBarras(codigoBarrasGenerado);
        
        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idAccesorioGenerado;
    }
        else{
        return 0;
        }
    }
  
    



    public void update(Accesorio a, String lastToken) throws Exception {
        
        if(validarToken(lastToken) == true){
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call actualizarAccesorio(  ?, ?, ?, ?, ?, ?, ? )}";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setString(1, a.getProducto().getCodigoBarras());
        cstmt.setString(2, a.getProducto().getNombre());
        cstmt.setString(3, a.getProducto().getMarca());
        cstmt.setDouble(4, a.getProducto().getPrecioCompra());
        cstmt.setDouble(5, a.getProducto().getPrecioVenta());
        cstmt.setInt(6, a.getProducto().getExistencias());

        cstmt.setInt(7, a.getProducto().getIdProducto());
        

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }}

    
     public void delete(Accesorio a, String lastToken) throws SQLException, Exception{
         if(validarToken(lastToken) == true){
        String sql = "{call eliminarAccesorio(?)}";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setInt(1, a.getProducto().getIdProducto());
        
        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        
        cstmt.close();
        connMySQL.close();    
    }
     }


    public List<Accesorio> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        
        
        String sql = "SELECT * FROM v_accesorios";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        //Para ejecutar instrucciones SQL
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();

        //Cada registro de empleado lo agregamos a una lista
        List<Accesorio> accesorios = new ArrayList<>();

        while (rs.next()) {
            accesorios.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

         return accesorios;
        
        
    }   
       
    

    private Accesorio fill(ResultSet rs) throws Exception {
        Accesorio a = new Accesorio();
        Producto p = new Producto();
 
        
        
        p.setIdProducto(rs.getInt("idProducto"));
        p.setCodigoBarras(rs.getString("codigoBarras"));
        p.setNombre(rs.getString("nombre"));
        p.setMarca(rs.getString("marca"));
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setExistencias(rs.getInt("existencias"));
     
p.setEstatus(rs.getInt("estatus"));
        
        

a.setIdAccesorio(rs.getInt("idAccesorio"));
        a.setProducto(p);

       
return a;
    }
    
    
/*public List<Accesorio> search(String filtro) throws SQLException, Exception{
        String sql = "SELECT * FROM v_accesorios WHERE idAccesorio LIKE '%"+filtro+"%' idProducto LIKE '%"+filtro+"%' OR codigoBarras LIKE '%"+filtro+"%' OR nombre LIKE '%"+filtro+"%' OR marca LIKE '%"+filtro+"%' OR precioCompra LIKE '%"+filtro+"%' OR precioVenta LIKE '%"+filtro+"%' OR existencias LIKE '%"+filtro+"%' OR estatus LIKE '%"+filtro+"%'";
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Accesorio> accesorios = new ArrayList<>();
        
        while (rs.next())
            accesorios.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return accesorios;
    }*/


}

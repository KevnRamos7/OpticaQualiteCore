/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;

import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Producto;
import com.redata.oq.model.Solucion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;

/**
 *
 * @author GTO
 */
public class ControllerSolucion {
    
    public int insert(Solucion s) throws Exception
    {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql =    "{call insertarSolucion(?, ?, ?, ?, ?, " + // Datos del producto
                                               "?, ?, ?)}";  // Valores de Retorno
        
        //Aquí guardaremos los ID's que se generarán:
        int idProductoGenerado = -1; //Se declaran las variables y se establecen a -1 como un valor fuera de rango
        int idSolucionGenerado = -1;
        String codigoBarrasGenerado = "";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql); //Se hace una instancia de un callableStatement a traves de conn.prepareCall()
        
        //Establecemos los valores de los parámetros de los datos personales 
        //en el orden en que los pide el procedimiento almacenado, 
        //comenzando en 1:
       
        
        cstmt.setString(1, s.getProducto().getNombre());
        cstmt.setString(2, s.getProducto().getMarca());
        cstmt.setDouble(3, s.getProducto().getPrecioCompra());
        cstmt.setDouble(4, s.getProducto().getPrecioVenta());
        cstmt.setInt(5, s.getProducto().getExistencias());
       
        
        
        //Registramos los parámetros de salida:
        cstmt.registerOutParameter(6, Types.INTEGER);
        cstmt.registerOutParameter(7, Types.INTEGER);
        cstmt.registerOutParameter(8, Types.VARCHAR);
        
        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        
        //Recuperamos los ID's generados:
        idProductoGenerado = cstmt.getInt(6);
        idSolucionGenerado = cstmt.getInt(7);        
        codigoBarrasGenerado = cstmt.getString(8);
        
        s.setIdSolucion(idSolucionGenerado);
        s.getProducto().setIdProducto(idProductoGenerado);
        s.getProducto().setCodigoBarras(codigoBarrasGenerado);
        
        cstmt.close();
        connMySQL.close();
        
        //Devolvemos el ID de Cliente generado:
        return idSolucionGenerado;
    }
    
    public void update(Solucion s) throws Exception
    {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql =    "{call actualizarSolucion(  ?, ?, ?, ?, ?, ?, ?)}";  //Datos del producto
        
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setString(1, s.getProducto().getCodigoBarras());
        cstmt.setString(2, s.getProducto().getNombre());
        cstmt.setString(3, s.getProducto().getMarca());
        cstmt.setDouble(4, s.getProducto().getPrecioCompra());
        cstmt.setDouble(5, s.getProducto().getPrecioVenta());
        cstmt.setInt(6, s.getProducto().getExistencias());
        cstmt.setInt(7, s.getProducto().getIdProducto());
        
        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        
        cstmt.close();
        connMySQL.close();        
    }
    
    public void delete(Solucion s) throws SQLException{
        String sql = "{call eliminarSolucion( ?)}";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setInt(1, s.getProducto().getIdProducto());
        
        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        
        cstmt.close();
        connMySQL.close();    
    }
    
    public List<Solucion> getAll(String filtro) throws Exception
    {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM v_soluciones";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Solucion> soluciones = new ArrayList<>();
        
        while (rs.next())
            soluciones.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return soluciones;
    }
    
    private Solucion fill(ResultSet rs) throws Exception
    {
        Solucion s = new Solucion();
        Producto p = new Producto();
        
        p.setIdProducto(rs.getInt("idProducto"));
        p.setCodigoBarras(rs.getString("codigoBarras"));
        p.setNombre(rs.getString("nombre"));
        p.setMarca(rs.getString("marca"));
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setExistencias(rs.getInt("existencias"));
        p.setEstatus(rs.getInt("estatus"));
        
        s.setIdSolucion(rs.getInt("idSolucion"));
        s.setProducto(p);
        
        return s;
    }
    
     public List<Solucion> search(String filtro) throws SQLException, Exception{
        String sql = "SELECT * FROM v_soluciones WHERE idSolucion LIKE '%"+filtro+"%' OR nombre LIKE '%"+filtro+"%' OR idProducto LIKE '%"+filtro+"%' OR codigoBarras LIKE '%"+filtro+"%' OR marca LIKE '%"+filtro+"%' OR precioCompra LIKE '%"+filtro+"%' OR precioVenta LIKE '%"+filtro+"%' OR existencias LIKE '%"+filtro+"%'";
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Solucion> empleados = new ArrayList<>();
        
        while (rs.next())
            empleados.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return empleados;
    }

}
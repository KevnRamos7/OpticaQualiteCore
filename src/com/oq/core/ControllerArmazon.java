/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;
import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Producto;
import com.redata.oq.model.Armazon;
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
 * @author alexi
 */
public class ControllerArmazon {
    
    public int insert(Armazon a) throws Exception
    {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql =    "{call insertarArmazon(?, ?, ?, ?, ?, ?, ?, ?, ?," + // Datos del producto
                                               "?, ?, ?, ?)}";  // Valores de Retorno
        
        //Aquí guardaremos los ID's que se generarán:
        int idProductoGenerado = -1; //Se declaran las variables y se establecen a -1 como un valor fuera de rango
        int idArmazonGenerado = -1;
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
         //el tipo de variable se obtiene del store procedure
        cstmt.setString(1, a.getProducto().getNombre());
        cstmt.setString(2, a.getProducto().getMarca());
        cstmt.setDouble(3, a.getProducto().getPrecioCompra());
        cstmt.setDouble(4, a.getProducto().getPrecioVenta());
        cstmt.setInt(5, a.getProducto().getExistencias());
        cstmt.setString(6, a.getFotografia());
        cstmt.setString(7, a.getModelo());
        cstmt.setString(8, a.getColor());
        cstmt.setString(9, a.getDimensiones());
        cstmt.setString(10, a.getDescripcion());
        
        
        
        

//Registramos los parámetros de salida:
        
        
        cstmt.registerOutParameter(11, Types.INTEGER);
        cstmt.registerOutParameter(12, Types.INTEGER);
        cstmt.registerOutParameter(13, Types.VARCHAR);
        
        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        
        //Recuperamos los ID's generados:
        idProductoGenerado = cstmt.getInt(11);
        idArmazonGenerado = cstmt.getInt(12);        
        codigoBarrasGenerado = cstmt.getString(13);
        
        a.setIdArmazon(idArmazonGenerado);
        a.getProducto().setIdProducto(idProductoGenerado);
        a.getProducto().setCodigoBarras(codigoBarrasGenerado);
        
        cstmt.close();
        connMySQL.close();
        
        //Devolvemos el ID de Cliente generado:
        return idArmazonGenerado;
    }
    
    public void update(Armazon a) throws Exception
    {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql =    "{call actualizarArmazon( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";  //Datos del producto
        
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
 
        cstmt.setString(1, a.getProducto().getNombre());
        cstmt.setString(2, a.getProducto().getMarca());
        cstmt.setDouble(3, a.getProducto().getPrecioCompra());
        cstmt.setDouble(4, a.getProducto().getPrecioVenta());
        cstmt.setInt(5, a.getProducto().getExistencias());
        cstmt.setString(6, a.getModelo());
        cstmt.setString(7, a.getColor());
        cstmt.setString(8, a.getDimensiones());
        cstmt.setString(9, a.getDescripcion());
        
        cstmt.setInt(10, a.getProducto().getIdProducto());
        cstmt.setInt(11, a.getIdArmazon());
        
        
        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        
        cstmt.close();
        connMySQL.close();        
    }
    
    public void delete(Armazon a) throws SQLException{
        String sql = "{call eliminarArmazon( ?)}";
        
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
    
    public List<Armazon> getAll(String filtro) throws Exception
    {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM v_armazones";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Armazon> armazones = new ArrayList<>();
        
        while (rs.next())
            armazones.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return armazones;
    }
    
    private Armazon fill(ResultSet rs) throws Exception
    {
        Armazon a = new Armazon();
        Producto p = new Producto();
        
        p.setIdProducto(rs.getInt("idProducto"));
        p.setCodigoBarras(rs.getString("codigoBarras"));
        p.setNombre(rs.getString("nombre"));
        p.setMarca(rs.getString("marca"));
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setExistencias(rs.getInt("existencias"));
        p.setEstatus(rs.getInt("estatus"));
        a.setColor(rs.getString("color"));
        a.setModelo(rs.getString("modelo"));
        a.setDimensiones(rs.getString("dimensiones"));
        a.setDescripcion(rs.getString("descripcion"));
        a.setIdArmazon(rs.getInt("idArmazon"));
        a.setFotografia(rs.getString("fotografia"));
        a.setProducto(p);
        
        return a;
    }
    
    public List<Armazon> search(String filtro) throws SQLException, Exception{
        String sql = "SELECT * FROM v_armazones WHERE idArmazon LIKE '%"+filtro+"%' OR modelo LIKE '%"+filtro+"%' OR color LIKE '%"+filtro+"%' OR dimensiones LIKE '%"+filtro+"%' OR descripcion LIKE '%"+filtro+"%' OR idProducto LIKE '%"+filtro+"%' OR codigoBarras LIKE '%"+filtro+"%' OR nombre LIKE '%"+filtro+"%' OR marca LIKE '%"+filtro+"%' OR precioCompra LIKE '%"+filtro+"%' OR precioVenta LIKE '%"+filtro+"%' OR existencias LIKE '%"+filtro+"%' OR estatus LIKE '%"+filtro+"%'";
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Armazon> armazones = new ArrayList<>();
        
        while (rs.next())
            armazones.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return armazones;
    }

}

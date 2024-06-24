/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;
import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import com.redata.oq.model.Persona;
import java.sql.SQLException;


public class ControllerCliente {

    public int insert(Cliente c) throws Exception { //necesita como parametro un objeto tipo empleado para agregar a la bd
        //Definimos la consulta SQL que invoca al Stored Procedure:
        //Definimos la cadena String que contendra la estructura del Stored Procedure es decir la consulta sql
        String sql = "{call insertarCliente(?, ?, ?, ?, ?, ?, ?, "
                + // Datos Personales
                "?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?)}";  // Valores de Retorno

        //Aquí guardaremos los ID's que se generarán:
        //Definimos las variables que guardaran posteriormente los id y le asignamos un valor fuera de rango
        int idPersonaGenerado = -1;
        int idClienteGenerado = -1;
        String numeroUnicoGenerado = "";

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
        cstmt.setString(1, c.getPersona().getNombre());
        cstmt.setString(2, c.getPersona().getApellidoPaterno());
        cstmt.setString(3, c.getPersona().getApellidoMaterno());
        cstmt.setString(4, c.getPersona().getGenero());
        cstmt.setString(5, c.getPersona().getFechaNacimiento());
        cstmt.setString(6, c.getPersona().getCalle());
        cstmt.setString(7, c.getPersona().getNumero());
        cstmt.setString(8, c.getPersona().getColonia());
        cstmt.setString(9, c.getPersona().getCp());
        cstmt.setString(10, c.getPersona().getCiudad());
        cstmt.setString(11, c.getPersona().getEstado());
        cstmt.setString(12, c.getPersona().getTelCasa());
        cstmt.setString(13, c.getPersona().getTelMovil());
        cstmt.setString(14, c.getPersona().getEmail());

        //Registramos los parámetros de salida:
        //Types se importa de sql y se le define el tipo de datos de sql
        cstmt.registerOutParameter(15, Types.INTEGER);
        cstmt.registerOutParameter(16, Types.INTEGER);
        cstmt.registerOutParameter(17, Types.VARCHAR);
        

        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idPersonaGenerado = cstmt.getInt(15);
        idClienteGenerado = cstmt.getInt(16);
        numeroUnicoGenerado = cstmt.getString(17);

        c.setIdCliente(idClienteGenerado);
        c.getPersona().setIdPersona(idPersonaGenerado);
        c.setNumeroUnico(numeroUnicoGenerado);
                
        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idClienteGenerado;
    }

    public void update(Cliente e) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call actualizarCliente(  ?, ?, ?, ?, ?, ?, ?, "
                + //Datos Personales
                "?, ?, ?, ?, ?, ?, ?, "
                + "?, ?)}"; // IDs

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setString(1, e.getPersona().getNombre());
        cstmt.setString(2, e.getPersona().getApellidoPaterno());
        cstmt.setString(3, e.getPersona().getApellidoMaterno());
        cstmt.setString(4, e.getPersona().getGenero());
        cstmt.setString(5, e.getPersona().getFechaNacimiento());
        cstmt.setString(6, e.getPersona().getCalle());
        cstmt.setString(7, e.getPersona().getNumero());
        cstmt.setString(8, e.getPersona().getColonia());
        cstmt.setString(9, e.getPersona().getCp());
        cstmt.setString(10, e.getPersona().getCiudad());
        cstmt.setString(11, e.getPersona().getEstado());
        cstmt.setString(12, e.getPersona().getTelCasa());
        cstmt.setString(13, e.getPersona().getTelMovil());
        cstmt.setString(14, e.getPersona().getEmail());

        cstmt.setInt(15, e.getPersona().getIdPersona());
        cstmt.setInt(16, e.getIdCliente());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }


    public void delete(Cliente e) throws SQLException {
        String sql = "{call eliminarCliente( ?)}";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        
        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setInt(1, e.getPersona().getIdPersona());
        
        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        
        cstmt.close();
        connMySQL.close();    
    }

    public List<Cliente> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM v_clientes";

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
        List<Cliente> clientes = new ArrayList<>();

        while (rs.next()) {
            clientes.add(fill(rs));
        }

        //Cerramos conexion
        rs.close();
        pstmt.close();
        connMySQL.close();

        return clientes;
    }

    private Cliente fill(ResultSet rs) throws Exception {
        Cliente e = new Cliente();
        Persona p = new Persona();

        //Le establecemos a persona los valores
        p.setIdPersona(rs.getInt("idPersona"));
        p.setNombre(rs.getString("nombre"));
        p.setApellidoMaterno(rs.getString("apellidoMaterno"));
        p.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p.setCalle(rs.getString("calle"));
        p.setCiudad(rs.getString("ciudad"));
        p.setColonia(rs.getString("colonia"));
        p.setCp(rs.getString("cp"));
        p.setEmail(rs.getString("email"));
        p.setEstado(rs.getString("estado"));
        p.setFechaNacimiento(rs.getString("fechaNacimiento"));
        p.setGenero(rs.getString("genero"));        
        p.setNumero(rs.getString("numero"));
        p.setTelCasa(rs.getString("telcasa"));
        p.setTelMovil(rs.getString("telmovil"));        

        e.setIdCliente(rs.getInt("idCliente"));
        e.setNumeroUnico(rs.getString("numeroUnico"));
        e.setEstatus(rs.getInt("estatus"));
        e.setPersona(p);

        //Devolvemos cliente
        return e;
    }
    
    public List<Cliente> search(String filtro) throws SQLException, Exception{
        String sql = "SELECT * FROM v_clientes WHERE idPersona LIKE '%"+filtro+"%' OR nombre LIKE '%"+filtro+"%' OR apellidoPaterno LIKE '%"+filtro+"%' OR apellidoMaterno LIKE '%"+filtro+"%' OR genero LIKE '%"+filtro+"%' OR calle LIKE '%"+filtro+"%' OR numero LIKE '%"+filtro+"%' OR colonia LIKE '%"+filtro+"%' OR cp LIKE '%"+filtro+"%' OR ciudad LIKE '%"+filtro+"%' OR estado LIKE '%"+filtro+"%' OR telcasa LIKE '%"+filtro+"%' OR telmovil LIKE '%"+filtro+"%' OR email LIKE '%"+filtro+"%'";
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Cliente> clientes = new ArrayList<>();
        
        while (rs.next())
            clientes.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return clientes;
    }

}
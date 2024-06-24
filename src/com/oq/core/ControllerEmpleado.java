package com.oq.core;

import com.redata.oq.db.ConexionMySQL;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import com.redata.oq.model.Persona;
import com.redata.oq.model.Usuario;
import com.redata.oq.model.Empleado;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LiveGrios
 */
public class ControllerEmpleado {

    public static boolean isAdmin(Empleado e) {
        if (e == null || e.getUsuario() == null || e.getUsuario().getNombre() == null) {
            return false;
        } else {
            return e.getUsuario().getRol().trim().toLowerCase().equals("administrador");
        }
    }

    public int insert(Empleado e) throws Exception { //necesita como parametro un objeto tipo empleado para agregar a la bd
        //Definimos la consulta SQL que invoca al Stored Procedure:
        //Definimos la cadena String que contendra la estructura del Stored Procedure es decir la consulta sql
        String sql = "{call insertarEmpleado(?, ?, ?, ?, ?, ?, ?, "
                + // Datos Personales
                "?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?,"
                + // Datos de Seguridad
                "?, ?, ?, ?, ?)}";  // Valores de Retorno

        //Aquí guardaremos los ID's que se generarán:
        //Definimos las variables que guardaran posteriormente los id y le asignamos un valor fuera de rango
        int idPersonaGenerado = -1;
        int idEmpleadoGenerado = -1;
        int idUsuarioGenerado = -1;
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

        // Registramos parámetros de datos de seguridad:
        cstmt.setString(15, e.getUsuario().getNombre());
        cstmt.setString(16, e.getUsuario().getContrasenia());
        cstmt.setString(17, e.getUsuario().getRol());
        cstmt.setString(18, e.getUsuario().getFotografia());

        //Registramos los parámetros de salida:
        //Types se importa de sql y se le define el tipo de datos de sql
        cstmt.registerOutParameter(19, Types.INTEGER);
        cstmt.registerOutParameter(20, Types.INTEGER);
        cstmt.registerOutParameter(21, Types.INTEGER);
        cstmt.registerOutParameter(22, Types.VARCHAR);
        cstmt.registerOutParameter(23, Types.VARCHAR);

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idPersonaGenerado = cstmt.getInt(19);
        idUsuarioGenerado = cstmt.getInt(20);
        idEmpleadoGenerado = cstmt.getInt(21);
        numeroUnicoGenerado = cstmt.getString(22);

        e.setIdEmpleado(idEmpleadoGenerado);
        e.getPersona().setIdPersona(idPersonaGenerado);
        e.getUsuario().setIdUsuario(idUsuarioGenerado);
        e.setNumeroUnico(numeroUnicoGenerado);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idEmpleadoGenerado;
    }

    public void delete(Empleado e) throws SQLException{
        String sql = "{call eliminarEmpleado( ?)}";
        
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

    public void update(Empleado e) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call actualizarEmpleado(  ?, ?, ?, ?, ?, ?, ?, "
                + //Datos Personales
                "?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?,"
                + // Datos de Seguridad
                "?, ?, ?)}"; // IDs

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
        cstmt.setString(15, e.getUsuario().getNombre());
        cstmt.setString(16, e.getUsuario().getContrasenia());
        cstmt.setString(17, e.getUsuario().getRol());
        cstmt.setString(18, e.getUsuario().getFotografia());

        cstmt.setInt(19, e.getPersona().getIdPersona());
        cstmt.setInt(20, e.getUsuario().getIdUsuario());
        cstmt.setInt(21, e.getIdEmpleado());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }



    public List<Empleado> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM v_empleados";

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
        List<Empleado> empleados = new ArrayList<>();

        while (rs.next()) {
            empleados.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return empleados;
    }

    private Empleado fill(ResultSet rs) throws Exception {
        Empleado e = new Empleado();
        Persona p = new Persona();

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
        p.setIdPersona(rs.getInt("idPersona"));
        p.setNombre(rs.getString("nombre"));
        p.setNumero(rs.getString("numero"));
        p.setTelCasa(rs.getString("telcasa"));
        p.setTelMovil(rs.getString("telmovil"));
        e.setEstatus(rs.getInt("estatus"));

        e.setIdEmpleado(rs.getInt("idEmpleado"));
        e.setNumeroUnico(rs.getString("numeroUnico"));
        e.setUsuario(new Usuario());
        e.getUsuario().setContrasenia(rs.getString("contrasenia"));
        e.getUsuario().setIdUsuario(rs.getInt("idUsuario"));
        e.getUsuario().setNombre(rs.getString("nombreUsuario"));
        e.getUsuario().setRol(rs.getString("rol"));
        e.getUsuario().setFotografia(rs.getString("fotografia"));
        e.getUsuario().setLastToken(rs.getString("lastToken"));
        e.getUsuario().setDateLastToken(rs.getString("dateLastToken"));
        e.setNumeroUnico(rs.getString("numeroUnico"));

        e.setPersona(p);

        return e;
    }
    
 public List<Empleado> search(String filtro) throws SQLException, Exception{
        String sql = "SELECT * FROM v_empleados WHERE idPersona LIKE '%"+filtro+"%' OR nombre LIKE '%"+filtro+"%' OR apellidoPaterno LIKE '%"+filtro+"%' OR apellidoMaterno LIKE '%"+filtro+"%' OR genero LIKE '%"+filtro+"%' OR calle LIKE '%"+filtro+"%' OR numero LIKE '%"+filtro+"%' OR colonia LIKE '%"+filtro+"%' OR cp LIKE '%"+filtro+"%' OR ciudad LIKE '%"+filtro+"%' OR estado LIKE '%"+filtro+"%' OR telcasa LIKE '%"+filtro+"%' OR telmovil LIKE '%"+filtro+"%' OR email LIKE '%"+filtro+"%' OR nombreUsuario LIKE '%"+filtro+"%'";
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<Empleado> empleados = new ArrayList<>();
        
        while (rs.next())
            empleados.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return empleados;
    }

}

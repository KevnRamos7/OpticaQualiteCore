/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;

import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Empleado;
import com.redata.oq.model.Persona;
import com.redata.oq.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GTO
 */
public class ControllerAcceso {

    public Empleado acceder(Usuario u) throws Exception {
        //1. Generar la consulta
        String query = "SELECT * FROM v_empleados WHERE nombreUsuario=? AND contrasenia=?";
        //2. Establecer una conexión a la bd
        ConexionMySQL objConn = new ConexionMySQL();
        //3. Abrir la conexión
        Connection conn = objConn.open();
        // 4. Preparar la sentencia
        PreparedStatement pstm = conn.prepareStatement(query);
        // 5. Enviar los parametros
        pstm.setString(1, u.getNombre());
        pstm.setString(2, u.getContrasenia());
        // 6. Ejecutar la consulta
        ResultSet rs = pstm.executeQuery();
        // 7. Tratar los datos
        Empleado e = fill(rs);
        // 8. Cerrar todos los elementos de la conexión
        rs.close();
        pstm.close();
        conn.close();
        objConn.close();
        return e;
    }

    
    
    public Empleado fill(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        Persona p = new Persona();
        Usuario u = new Usuario();

        while (rs.next()) {

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

            u.setContrasenia(rs.getString("contrasenia"));
            u.setIdUsuario(rs.getInt("idUsuario"));
            u.setNombre(rs.getString("nombreUsuario"));
            u.setRol(rs.getString("rol"));
            u.setFotografia(rs.getString("fotografia"));
            u.setLastToken(rs.getString("lastToken"));
            u.setDateLastToken(rs.getString("dateLastToken"));

            e.setPersona(p);
            e.setUsuario(u);
        }
        return e;
    }

    public void guardarToken(Empleado e) throws SQLException {
        String query = "UPDATE usuario SET lastToken=?, dateLastToken=NOW() WHERE idUsuario=?";

        ConexionMySQL objConn = new ConexionMySQL();
        Connection conn = objConn.open();
        PreparedStatement pstm = conn.prepareCall(query);
        pstm.setString(1, e.getUsuario().getLastToken());
        pstm.setInt(2, e.getUsuario().getIdUsuario());
        pstm.execute();
        pstm.close();
        conn.close();
        objConn.close();
    }

    public boolean eliminarToken(Empleado empleado) throws SQLException {
        
        boolean r = false;
        String query = "UPDATE usuario SET lastToken = '', dateLastToken='0000-00-00 00:00:00' WHERE idUsuario=?";
        ConexionMySQL objConn = new ConexionMySQL();
        Connection conn = objConn.open();
        PreparedStatement pstm = conn.prepareCall(query);
        pstm.setInt(1, empleado.getUsuario().getIdUsuario());
        pstm.execute();
        r = true;
        pstm.close();
        conn.close();
        objConn.close();
        return r;
    }
    
 
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;
import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.Detalle_Venta_Producto;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author kevin
 */
public class ControllerVentaP {

    public boolean generarVenta(Detalle_Venta_Producto dvp) throws SQLException {
        boolean resultado = false;
        ConexionMySQL conMysql = new ConexionMySQL();
        Connection conn = conMysql.open();
        Statement stmnt = null;
        ResultSet rs = null;
        System.out.println("Datos recibidos: " + dvp);
        try {
            conn.setAutoCommit(false);
            stmnt = conn.createStatement();

            String query1 = "INSERT INTO venta (idEmpleado, clave) VALUES "
                    + "(" + dvp.getVenta().getEmpleado().getIdEmpleado()
                    + ", " + dvp.getVenta().getClave() + ");";
            stmnt.execute(query1);

            String query2 = "SELECT LAST_INSERT_ID();";
            rs = stmnt.executeQuery(query2);
            
            if (rs.next()) {
                dvp.getVenta().setIdVenta(rs.getInt(1));
            }

            for (int i = 0; i < dvp.getLvp().size(); i++) {
                String query3 = "INSERT INTO venta_producto VALUES ("
                        + dvp.getVenta().getIdVenta() + ", "
                        + dvp.getLvp().get(i).getProducto().getIdProducto() + ", "
                        + dvp.getLvp().get(i).getCantidad() + ", "
                        + dvp.getLvp().get(i).getPrecioUnitario() + ", "
                        + dvp.getLvp().get(i).getDescuento() + ");";

                stmnt.execute(query3);
            }

            conn.commit();
            conn.setAutoCommit(true);

            rs.close();
            stmnt.close();
            conn.close();
            conMysql.close();

            resultado = true;
        } catch (SQLException ex) {
            Logger.getLogger(ControllerVentaP.class.getName()).log(Level.SEVERE, null, ex);

            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.close();
                conMysql.close();

                resultado = false;
            } catch (SQLException ex1) {

            }
            resultado = false;
        }
        return resultado;
    }
}
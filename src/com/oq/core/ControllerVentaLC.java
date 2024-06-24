/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;
import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.DetalleVentaPre;
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
public class ControllerVentaLC {
    public boolean generarVentaLC(DetalleVentaPre dvp) {
        boolean r = false;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        Statement stmnt = null;
        ResultSet rs = null;

        System.out.println("Datos recibidos en controlador: " + dvp);

        try {
            conn.setAutoCommit(false);
            stmnt = conn.createStatement();
            // Se inserta la venta
            String query1 = "INSERT INTO venta (idEmpleado, clave) "
                    + "VALUES (" + dvp.getVenta().getEmpleado().getIdEmpleado()
                    + ',' + dvp.getVenta().getClave() + ");";
            stmnt.execute(query1);
            // Se obtiene el id de la venta que se ha insertado
            String query2 = "SELECT LAST_INSERT_ID()";
            rs = stmnt.executeQuery(query2);
            if (rs.next()) {
                dvp.getVenta().setIdVenta(rs.getInt(1));
            }

 

            for (int i = 0; i < dvp.getVenta_presupuesto().size(); i++) {
                //Se inserta el presupuesto
                String query3 = "INSERT INTO presupuesto"
                        + "(idExamenVista, clave)"
                        + "VALUES (" + dvp.getVenta_presupuesto().get(i).getPresupuestoLC().getExamenVista().getIdExamenVista()
                        + ",'" + dvp.getVenta_presupuesto().get(i).getPresupuestoLC().getPresupuesto().getClave() + " ');";
                stmnt.execute(query3);
                //Se obtiene el id del presupuesto generado
                rs = stmnt.executeQuery(query2);
                if (rs.next()) {
                    dvp.getVenta_presupuesto().get(i).getPresupuestoLC().getPresupuesto().setIdPresupuesto(rs.getInt(1));
                }

 

                //Se inserta en presupuesto_lentescontacto                
                String query4 = "INSERT INTO presupuesto_lentescontacto"
                        + "(idExamenVista, idLenteContacto, clave)"
                        + "VALUES (" + dvp.getVenta_presupuesto().get(i).getPresupuestoLC().getExamenVista().getIdExamenVista() + ","
                        + dvp.getVenta_presupuesto().get(i).getPresupuestoLC().getLenteContacto().getIdLenteContacto()+ ","
                        + dvp.getVenta_presupuesto().get(i).getPresupuestoLC().getClave() + ");";
                stmnt.execute(query4);
                //Se obtiene el id del presupuesto_lentescontacto generado
                rs = stmnt.executeQuery(query2);
                if (rs.next()) {
                    dvp.getVenta_presupuesto().get(i).getPresupuestoLC().setIdPresupuestoLC(rs.getInt(1));
                }

 

                //Se insera en venta_presupuesto la relación entre la venta y el presupuesto
                String query5 = "INSERT INTO venta_presupuesto "
                        + "(idVenta, idPresupuesto, cantidad, precioUnitario, descuento) "
                        + "VALUES ("
                        + dvp.getVenta().getIdVenta() + ","
                        + dvp.getVenta_presupuesto().get(i).getPresupuestoLC().getPresupuesto().getIdPresupuesto() + ","
                        + dvp.getVenta_presupuesto().get(i).getCantidad() + ","
                        + dvp.getVenta_presupuesto().get(i).getPrecioUnitario() + ","
                        + dvp.getVenta_presupuesto().get(i).getDescuento()+ ");";
                stmnt.execute(query5);
            }
            conn.commit();
            conn.setAutoCommit(true);
            stmnt.close();
            conn.close();
            r = true;

 

        } catch (SQLException ex) { 
            Logger.getLogger(ControllerVentaLC.class.getName()).log(Level.SEVERE, null, ex);
                   try {

 

                conn.rollback();
                conn.setAutoCommit(true);
                conn.close();
                r = false;
            } catch (SQLException ex1) {
                r = false;
            }
        }

 

        return r;
    }
}

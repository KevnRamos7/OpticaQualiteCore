/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oq.core;

import com.mysql.cj.protocol.Resultset;
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
public class ControllerVentaLentes {

    public boolean generarVentaLente(DetalleVentaPre dtvp) {
        boolean r = false;

        ConexionMySQL conMysql = new ConexionMySQL();
        Connection conn = conMysql.open();
        Statement stmt = null;
        ResultSet rs = null;
      
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String query = "SELECT LAST_INSERT_ID()";
            
            String query0 = "INSERT INTO venta (idEmpleado, clave) VALUES ("+dtvp.getVenta().getEmpleado().getIdEmpleado() + ", "+dtvp.getVenta().getClave()+")";
            stmt.execute(query0);
            rs = stmt.executeQuery(query);
             System.out.println("Datos recibidos: " + dtvp);
            if(rs.next()){
                dtvp.getVenta().setIdVenta(rs.getInt(1));
            }
            
            for (int i = 0; i < dtvp.getVenta_presupuesto().size(); i++) {
                conn.setAutoCommit(false);
                String query1 = "INSERT INTO presupuesto (idExamenVista ,clave) VALUES  (" + dtvp.getVenta_presupuesto().get(i).getPresupuesto().getExamenVista().getIdExamenVista() + ", '" + dtvp.getVenta_presupuesto().get(i).getPresupuesto().getClave() + "')";
                stmt.execute(query1);
                rs = stmt.executeQuery(query);

                if (rs.next()) {
                    dtvp.getVenta_presupuesto().get(i).getPresupuesto().setIdPresupuesto(rs.getInt(1));
                }
                String query2 = "INSERT INTO presupuesto_lentes (idPresupuesto, alturaOblea, idTipoMica, idMaterial, idArmazon) "
                        + "VALUES (" + dtvp.getVenta_presupuesto().get(i).getPresupuesto().getIdPresupuesto() + ""
                        + ", " + dtvp.getVenta_presupuesto().get(i).getPresupuesto().getPresupuesto_lentes().getAlturaOblea() + ""
                        + ", " + dtvp.getVenta_presupuesto().get(i).getPresupuesto().getPresupuesto_lentes().getMica().getIdTipoMica() + ""
                        + ", " + dtvp.getVenta_presupuesto().get(i).getPresupuesto().getPresupuesto_lentes().getMaterial().getIdMaterial() + ""
                        + ", " + dtvp.getVenta_presupuesto().get(i).getPresupuesto().getPresupuesto_lentes().getArmazon().getIdArmazon() + ")";

                stmt.execute(query2);
                rs = stmt.executeQuery(query);

                if (rs.next()) {
                    
                    dtvp.getVenta_presupuesto().get(i).getPresupuesto().getPresupuesto_lentes().setIdPresupuestoLentes(rs.getInt(1));
                    
                }

                for (int j = 0; j < dtvp.getVenta_presupuesto().get(i).getPresupuesto().getPresupuesto_lentes().getPresupuesto_lentes_tratamiento().size(); j++) {
                    String query3 = "INSERT INTO presupuesto_lentes_tratamientos VALUES (" + dtvp.getVenta_presupuesto().get(i).getPresupuesto().getPresupuesto_lentes().getIdPresupuestoLentes() + ""
                            + ", " + dtvp.getVenta_presupuesto().get(i).getPresupuesto().getPresupuesto_lentes().getPresupuesto_lentes_tratamiento().get(j).getTratamiento().getIdTratamiento() + ")";
               
                stmt.execute(query3);
                }
               
                
                
                String query4 = "INSERT INTO venta_presupuesto VALUES("+dtvp.getVenta().getIdVenta()+""
                        + ", "+dtvp.getVenta_presupuesto().get(i).getPresupuesto().getIdPresupuesto()+""
                        + ", "+dtvp.getVenta_presupuesto().get(i).getCantidad()+""
                        + ", "+dtvp.getVenta_presupuesto().get(i).getPrecioUnitario()+""
                        + ", "+dtvp.getVenta_presupuesto().get(i).getDescuento()+")";
                
                stmt.executeUpdate(query4);
               
            }
            conn.commit();
            rs.close();
            stmt.close();
            conn.close();
            conMysql.close();
            r = true;

        } catch (SQLException ex) {
            Logger.getLogger(ControllerVentaLentes.class.getName()).log(Level.SEVERE, null, ex);
            try{
                conn.rollback();
                conn.setAutoCommit(true);
                rs.close();
                stmt.close();
                conn.close();
                conMysql.close();
                r = false;
            } catch (SQLException ex1) {
                Logger.getLogger(ControllerVentaLentes.class.getName()).log(Level.SEVERE, null, ex1);
                r = false;
            }
        }
        return r;
    }
}

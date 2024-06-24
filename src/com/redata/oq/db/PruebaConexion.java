/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redata.oq.db;

/**
 *
 * @author kevin
 */
public class PruebaConexion {
    
    public static void main(String [] args){
        ConexionMySQL connMySQL = new ConexionMySQL();
        try{
            connMySQL.open();
            System.out.println("Conexion Establecida");
            
            connMySQL.close();
            System.out.println("Se cerro la conexion");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

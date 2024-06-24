package com.oq.core;

import com.redata.oq.db.ConexionMySQL;
import com.redata.oq.model.LenteContacto;
import com.redata.oq.model.Producto;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marib
 */
public class ControllerLentesContacto {

    public int insert(LenteContacto l) throws SQLException {
        String sql = "{call insertarLentesContacto(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        int idProductoGenerado = -1;
        int idLenteContactoGenerado = -1;
        String codigoBarrasGenerado = "";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

       
        cstmt.setString(1, l.getProducto().getNombre());
        cstmt.setString(2, l.getProducto().getMarca());
        cstmt.setDouble(3, l.getProducto().getPrecioCompra());
        cstmt.setDouble(4, l.getProducto().getPrecioVenta());
        cstmt.setInt(5, l.getProducto().getExistencias());
         cstmt.setString(6, l.getFotografia());
        cstmt.setInt(7, l.getKeratometria());
       

        cstmt.registerOutParameter(8, Types.INTEGER);
        cstmt.registerOutParameter(9, Types.INTEGER);
        cstmt.registerOutParameter(10, Types.VARCHAR);

        cstmt.executeUpdate();

        idProductoGenerado = cstmt.getInt(8);
        idLenteContactoGenerado = cstmt.getInt(9);
        codigoBarrasGenerado = cstmt.getString(10);

        l.setIdLenteContacto(idLenteContactoGenerado);
        l.getProducto().setIdProducto(idProductoGenerado);
        l.getProducto().setCodigoBarras(codigoBarrasGenerado);

        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de Cliente generado:
        return idLenteContactoGenerado;
    }

    public void delete(LenteContacto l) throws SQLException {
        String sql = "{call eliminarLenteContacto(?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setInt(1, l.getProducto().getIdProducto());
        cstmt.executeUpdate();
        cstmt.close();
        connMySQL.close();
    }

    public void update(LenteContacto l) throws Exception {
        String sql = "{call actualizarLentesContacto(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setString(1, l.getProducto().getCodigoBarras());
        cstmt.setString(2, l.getProducto().getNombre());
        cstmt.setString(3, l.getProducto().getMarca());
        cstmt.setDouble(4, l.getProducto().getPrecioCompra());
        cstmt.setDouble(5, l.getProducto().getPrecioVenta());
        cstmt.setInt(6, l.getProducto().getExistencias());
        cstmt.setInt(7, l.getKeratometria());

        cstmt.setInt(8, l.getProducto().getIdProducto());
        cstmt.setInt(9, l.getIdLenteContacto());

        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public List<LenteContacto> getAll(String filtro) throws Exception {
        String sql = "SELECT * FROM v_lentes_contacto";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<LenteContacto> lentes = new ArrayList<>();

        while (rs.next()) {
            lentes.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return lentes;
    }

    private LenteContacto fill(ResultSet rs) throws Exception {
        LenteContacto lc = new LenteContacto();
        Producto p = new Producto();

        lc.setIdLenteContacto(rs.getInt("idLenteContacto"));
        lc.setKeratometria(rs.getInt("keratometria"));

        p.setIdProducto(rs.getInt("idProducto"));
        p.setCodigoBarras(rs.getString("codigoBarras"));
        p.setNombre(rs.getString("Nombre"));
        p.setMarca(rs.getString("Marca"));
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setExistencias(rs.getInt("existencias"));
        p.setEstatus(rs.getInt("estatus"));
        lc.setFotografia(rs.getString("fotografia"));
        
        lc.setProducto(p);
        
        return lc;
    }
    
     public List<LenteContacto> search(String filtro) throws SQLException, Exception{
        String sql = "SELECT * FROM v_lentes_contacto WHERE idLenteContacto LIKE '%"+filtro+"%' OR nombre LIKE '%"+filtro+"%' OR keratometria LIKE '%"+filtro+"%' OR idProducto LIKE '%"+filtro+"%' OR codigoBarras LIKE '%"+filtro+"%' OR marca LIKE '%"+filtro+"%' OR precioCompra LIKE '%"+filtro+"%' OR precioVenta LIKE '%"+filtro+"%' OR existencias LIKE '%"+filtro+"%'";
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql); //prepared sirve para hacer instrucciones sql y callable para intrucciones no sql
        
        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        
        List<LenteContacto> len = new ArrayList<>();
        
        while (rs.next())
            len.add(fill(rs));
        
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        return len;
    }

}
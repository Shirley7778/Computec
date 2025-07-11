package DAO.impl;

import DAO.MovimientoInventarioDAO;
import Modelo.MovimientoInventario;
import ConexionBD.Conexion;
import java.util.Collections;
import java.util.List;

public class MovimientoInventarioDAOImpl implements MovimientoInventarioDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(MovimientoInventario m) throws Exception {
        String sql = "INSERT INTO movimiento_inventario (stock_id, pedido_id, tipo, cantidad, motivo, referencia, observaciones, ubicacion_proveedor, ubicacion_fisica_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, m.getStock() != null ? m.getStock().getStockId() : 0);
            ps.setObject(2, m.getPedido() != null ? m.getPedido().getIdPedido() : null, java.sql.Types.BIGINT);
            ps.setString(3, m.getTipo());
            ps.setInt(4, m.getCantidad());
            ps.setString(5, m.getMotivo());
            ps.setString(6, m.getReferencia());
            ps.setString(7, m.getObservaciones());
            ps.setString(8, m.getUbicacionProveedor());
            ps.setObject(9, m.getUbicacionFisica() != null ? m.getUbicacionFisica().getUbicacionId() : null, java.sql.Types.BIGINT);
            ps.executeUpdate();
        }
    }

    @Override
    public MovimientoInventario findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MovimientoInventario> findAll() throws Exception {
        String sql = "SELECT m.*, s.stock_id, p.producto_id, p.nombre AS producto_nombre, u.ubicacion_id, u.mi_ubicacion_fisica " +
                     "FROM movimiento_inventario m " +
                     "LEFT JOIN stock s ON m.stock_id = s.stock_id " +
                     "LEFT JOIN productos p ON s.producto_id = p.producto_id " +
                     "LEFT JOIN ubicacion_fisica u ON m.ubicacion_fisica_id = u.ubicacion_id";
        java.util.List<Modelo.MovimientoInventario> lista = new java.util.ArrayList<>();
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Modelo.MovimientoInventario mov = new Modelo.MovimientoInventario();
                mov.setMovimientoId(rs.getLong("movimiento_id"));
                mov.setTipo(rs.getString("tipo"));
                mov.setCantidad(rs.getInt("cantidad"));
                mov.setMotivo(rs.getString("motivo"));
                mov.setReferencia(rs.getString("referencia"));
                mov.setObservaciones(rs.getString("observaciones"));
                mov.setUbicacionProveedor(rs.getString("ubicacion_proveedor"));
                // Mapear producto y stock
                Modelo.Producto producto = new Modelo.Producto();
                producto.setProductoId(rs.getLong("producto_id"));
                producto.setNombre(rs.getString("producto_nombre"));
                Modelo.Stock stock = new Modelo.Stock();
                stock.setStockId(rs.getLong("stock_id"));
                stock.setProducto(producto);
                mov.setStock(stock);
                // Mapear ubicación física
                Modelo.UbicacionFisica ubicacion = new Modelo.UbicacionFisica();
                ubicacion.setUbicacionId(rs.getLong("ubicacion_id"));
                ubicacion.setMiUbicacionFisica(rs.getString("mi_ubicacion_fisica"));
                mov.setUbicacionFisica(ubicacion);
                lista.add(mov);
            }
        }
        return lista;
    }

    @Override
    public void update(MovimientoInventario m) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(long id) throws Exception {
        String sql = "DELETE FROM movimiento_inventario WHERE movimiento_id = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}

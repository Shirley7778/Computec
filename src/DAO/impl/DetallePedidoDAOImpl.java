package DAO.impl;

import DAO.DetallePedidoDAO;
import Modelo.DetallePedido;
import ConexionBD.Conexion;
import Modelo.Pedido;
import Modelo.Producto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetallePedidoDAOImpl implements DetallePedidoDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(DetallePedido d) throws Exception {
        String sql = "INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, total) VALUES (?, ?, ?, ?, ?)";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, d.getPedido().getIdPedido());
            ps.setLong(2, d.getProducto().getProductoId());
            ps.setInt(3, d.getCantidad());
            ps.setBigDecimal(4, d.getPrecioUnitario());
            ps.setBigDecimal(5, d.getTotal());
            ps.executeUpdate();
        }
    }

    @Override
    public DetallePedido findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DetallePedido> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public List<DetallePedido> findByPedidoId(long pedidoId) throws Exception {
        String sql = "SELECT dp.detalle_id, dp.pedido_id, dp.producto_id, dp.cantidad, dp.precio_unitario, dp.total, " +
                    "p.nombre as producto_nombre, p.categoria as producto_categoria, p.precio as producto_precio " +
                    "FROM detalle_pedido dp " +
                    "INNER JOIN productos p ON dp.producto_id = p.producto_id " +
                    "WHERE dp.pedido_id = ?";
        
        List<DetallePedido> detalles = new ArrayList<>();
        
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setLong(1, pedidoId);
            java.sql.ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                DetallePedido detalle = new DetallePedido();
                detalle.setDetalleId(rs.getLong("detalle_id"));
                
                // Crear pedido con solo el ID
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getLong("pedido_id"));
                detalle.setPedido(pedido);
                
                // Crear producto con datos completos
                Producto producto = new Producto();
                producto.setProductoId(rs.getLong("producto_id"));
                producto.setNombre(rs.getString("producto_nombre"));
                producto.setCategoria(rs.getString("producto_categoria"));
                producto.setPrecio(rs.getBigDecimal("producto_precio"));
                detalle.setProducto(producto);
                
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                detalle.setTotal(rs.getBigDecimal("total"));
                
                detalles.add(detalle);
            }
        }
        
        return detalles;
    }

    @Override
    public void update(DetallePedido d) throws Exception {
        String sql = "UPDATE detalle_pedido SET cantidad = ?, precio_unitario = ?, total = ? WHERE detalle_id = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, d.getCantidad());
            ps.setBigDecimal(2, d.getPrecioUnitario());
            ps.setBigDecimal(3, d.getTotal());
            ps.setLong(4, d.getDetalleId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

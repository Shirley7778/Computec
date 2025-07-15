package DAO.impl;

import DAO.StockDAO;
import Modelo.Stock;
import ConexionBD.Conexion;
import java.util.List;
import java.sql.Connection;
import Modelo.Producto;

public class StockDAOImpl implements StockDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(Stock s) throws Exception {
        String sql = "INSERT INTO stock (producto_id, cantidad_actual, minimo) VALUES (?, ?, ?)";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, s.getProducto().getProductoId());
            ps.setInt(2, s.getCantidadActual());
            ps.setInt(3, s.getMinimo());
            ps.executeUpdate();
            try (java.sql.ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    s.setStockId(rs.getLong(1));
                }
            }
        }
    }

    @Override
    public Stock findById(long productoId) throws Exception {
        String sql = "SELECT * FROM stock WHERE producto_id = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, productoId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Modelo.Stock stock = new Modelo.Stock();
                    stock.setStockId(rs.getLong("stock_id"));
                    // Relacionar producto si es necesario
                    stock.setCantidadActual(rs.getInt("cantidad_actual"));
                    stock.setMinimo(rs.getInt("minimo"));
                    return stock;
                }
            }
        }
        return null;
    }

    @Override
    public List<Stock> findAll() throws Exception {
        String sql = "SELECT s.*, p.nombre, p.categoria, p.precio FROM stock s INNER JOIN productos p ON s.producto_id = p.producto_id";
        List<Stock> lista = new java.util.ArrayList<>();
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Modelo.Producto producto = new Modelo.Producto();
                producto.setProductoId(rs.getLong("producto_id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                Modelo.Stock stock = new Modelo.Stock();
                stock.setStockId(rs.getLong("stock_id"));
                stock.setProducto(producto);
                stock.setCantidadActual(rs.getInt("cantidad_actual"));
                stock.setMinimo(rs.getInt("minimo"));
                lista.add(stock);
            }
        }
        return lista;
    }

    @Override
    public void update(Stock s) throws Exception {
        String sql = "UPDATE stock SET cantidad_actual = ?, minimo = ? WHERE stock_id = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, s.getCantidadActual());
            ps.setInt(2, s.getMinimo());
            ps.setLong(3, s.getStockId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Stock findByProductoNombre(String nombre) throws Exception {
        String sql = "SELECT s.* FROM stock s INNER JOIN productos p ON s.producto_id = p.producto_id WHERE p.nombre = ?";
        try (Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        }
        return null;
    }
    
    private Stock map(java.sql.ResultSet rs) throws java.sql.SQLException {
        Stock stock = new Stock();
        stock.setStockId(rs.getLong("stock_id"));
        stock.setCantidadActual(rs.getInt("cantidad_actual"));
        stock.setMinimo(rs.getInt("minimo"));
        // Mapear el producto
        Producto producto = new Producto();
        producto.setProductoId(rs.getLong("producto_id"));
        // El nombre, categoria y precio no están en el ResultSet, pero puedes hacer una consulta adicional si lo necesitas
        stock.setProducto(producto);
        return stock;
    }
}

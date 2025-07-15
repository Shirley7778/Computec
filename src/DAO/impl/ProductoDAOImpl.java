package DAO.impl;

import DAO.ProductoDAO;
import Modelo.Producto;
import ConexionBD.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    private final Conexion conexion = new Conexion();

    @Override
    public void create(Producto p) throws Exception {
        String sql = "INSERT INTO productos(nombre,categoria,precio) VALUES(?,?,?)";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setBigDecimal(3, p.getPrecio());
            ps.executeUpdate();
        }
    }

    @Override
    public Producto findById(long id) throws Exception {
        String sql = "SELECT * FROM productos WHERE producto_id=?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Producto p = new Producto();
                p.setProductoId(rs.getLong("producto_id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecio(rs.getBigDecimal("precio"));
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Producto> findAll() throws Exception {
        String sql = "SELECT * FROM productos";
        List<Producto> lista = new ArrayList<>();
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setProductoId(rs.getLong("producto_id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecio(rs.getBigDecimal("precio"));
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public void update(Producto p) throws Exception {
        String sql = "UPDATE productos SET nombre=?, categoria=?, precio=? WHERE producto_id=?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setBigDecimal(3, p.getPrecio());
            ps.setLong(4, p.getProductoId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws Exception {
        String sql = "DELETE FROM productos WHERE producto_id=?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Producto findByNombre(String nombre) throws Exception {
        String sql = "SELECT * FROM productos WHERE nombre=?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Producto p = new Producto();
                p.setProductoId(rs.getLong("producto_id"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecio(rs.getBigDecimal("precio"));
                return p;
            }
        }
        return null;
    }
}

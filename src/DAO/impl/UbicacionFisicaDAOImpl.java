package DAO.impl;

import DAO.UbicacionFisicaDAO;
import Modelo.UbicacionFisica;
import ConexionBD.Conexion;
import java.util.ArrayList;
import java.util.List;

public class UbicacionFisicaDAOImpl implements UbicacionFisicaDAO {
   private final Conexion conexion = new Conexion();

    @Override
    public void create(UbicacionFisica u) throws Exception {
        String sql = "INSERT INTO ubicacion_fisica (mi_ubicacion_fisica, capacidad, tipo, estado, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getMiUbicacionFisica());
            ps.setObject(2, u.getCapacidad());
            ps.setString(3, u.getTipo());
            ps.setString(4, u.getEstado());
            ps.setString(5, u.getDescripcion());
            ps.executeUpdate();
            
            java.sql.ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                u.setUbicacionId(rs.getLong(1));
            }
        }
    }

    @Override
    public UbicacionFisica findById(long id) throws Exception {
        String sql = "SELECT * FROM ubicacion_fisica WHERE ubicacion_id = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        }
        return null;
    }

    @Override
    public List<UbicacionFisica> findAll() throws Exception {
        String sql = "SELECT * FROM ubicacion_fisica ORDER BY ubicacion_id";
        List<UbicacionFisica> ubicaciones = new ArrayList<>();
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ubicaciones.add(map(rs));
            }
        }
        return ubicaciones;
    }

    @Override
    public void update(UbicacionFisica u) throws Exception {
        String sql = "UPDATE ubicacion_fisica SET mi_ubicacion_fisica = ?, capacidad = ?, tipo = ?, estado = ?, descripcion = ? WHERE ubicacion_id = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getMiUbicacionFisica());
            ps.setObject(2, u.getCapacidad());
            ps.setString(3, u.getTipo());
            ps.setString(4, u.getEstado());
            ps.setString(5, u.getDescripcion());
            ps.setLong(6, u.getUbicacionId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws Exception {
        String sql = "DELETE FROM ubicacion_fisica WHERE ubicacion_id = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public UbicacionFisica findByNombre(String nombre) throws Exception {
        String sql = "SELECT * FROM ubicacion_fisica WHERE mi_ubicacion_fisica = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        }
        return null;
    }

    private UbicacionFisica map(java.sql.ResultSet rs) throws java.sql.SQLException {
        UbicacionFisica u = new UbicacionFisica();
        u.setUbicacionId(rs.getLong("ubicacion_id"));
        u.setMiUbicacionFisica(rs.getString("mi_ubicacion_fisica"));
        u.setCapacidad(rs.getObject("capacidad") != null ? rs.getInt("capacidad") : null);
        u.setTipo(rs.getString("tipo"));
        u.setEstado(rs.getString("estado"));
        u.setDescripcion(rs.getString("descripcion"));
        return u;
    }
}

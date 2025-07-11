package DAO.impl;

import DAO.UbicacionFisicaDAO;
import Modelo.UbicacionFisica;
import ConexionBD.Conexion;
import java.util.Collections;
import java.util.List;

public class UbicacionFisicaDAOImpl implements UbicacionFisicaDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(UbicacionFisica u) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UbicacionFisica findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UbicacionFisica> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void update(UbicacionFisica u) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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

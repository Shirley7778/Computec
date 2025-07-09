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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MovimientoInventario findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MovimientoInventario> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void update(MovimientoInventario m) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

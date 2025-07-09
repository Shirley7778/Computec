package DAO.impl;

import DAO.ReclamoDAO;
import Modelo.Reclamo;
import ConexionBD.Conexion;
import java.util.Collections;
import java.util.List;

public class ReclamoDAOImpl implements ReclamoDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(Reclamo r) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Reclamo findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Reclamo> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void update(Reclamo r) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

package DAO.impl;

import DAO.PermisoDAO;
import Modelo.Permiso;
import ConexionBD.Conexion;
import java.util.Collections;
import java.util.List;

public class PermisoDAOImpl implements PermisoDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(Permiso p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Permiso findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Permiso> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void update(Permiso p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

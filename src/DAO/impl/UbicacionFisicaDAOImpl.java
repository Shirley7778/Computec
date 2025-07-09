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
}

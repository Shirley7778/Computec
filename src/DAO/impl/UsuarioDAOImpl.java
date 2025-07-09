package DAO.impl;

import DAO.UsuarioDAO;
import Modelo.Usuario;
import ConexionBD.Conexion;
import java.util.Collections;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(Usuario u) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Usuario findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Usuario> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void update(Usuario u) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

package DAO.impl;

import DAO.PedidoDAO;
import Modelo.Pedido;
import ConexionBD.Conexion;
import java.util.Collections;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(Pedido p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Pedido findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Pedido> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void update(Pedido p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

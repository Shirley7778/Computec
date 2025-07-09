package DAO.impl;

import DAO.DetallePedidoDAO;
import Modelo.DetallePedido;
import ConexionBD.Conexion;
import java.util.Collections;
import java.util.List;

public class DetallePedidoDAOImpl implements DetallePedidoDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(DetallePedido d) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DetallePedido findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DetallePedido> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void update(DetallePedido d) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

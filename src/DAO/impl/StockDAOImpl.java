package DAO.impl;

import DAO.StockDAO;
import Modelo.Stock;
import ConexionBD.Conexion;
import java.util.Collections;
import java.util.List;

public class StockDAOImpl implements StockDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(Stock s) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Stock findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Stock> findAll() throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void update(Stock s) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

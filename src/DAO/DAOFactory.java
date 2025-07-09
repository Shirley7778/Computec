package DAO;

import DAO.impl.ClienteDAOImpl;
import DAO.impl.ProductoDAOImpl;

public class DAOFactory {
    public static ClienteDAO getClienteDAO() {
        return new ClienteDAOImpl();
    }

    public static ProductoDAO getProductoDAO() {
        return new ProductoDAOImpl();
    }
}

package DAO;

import DAO.impl.ClienteDAOImpl;
import DAO.impl.ProductoDAOImpl;
import DAO.impl.PedidoDAOImpl;
import DAO.impl.DetallePedidoDAOImpl;
import DAO.impl.ReclamoDAOImpl;
import DAO.impl.StockDAOImpl;
import DAO.impl.MovimientoInventarioDAOImpl;
import DAO.impl.UbicacionFisicaDAOImpl;
import DAO.impl.PermisoDAOImpl;
import DAO.impl.UsuarioDAOImpl;

import DAO.PedidoDAO;
import DAO.DetallePedidoDAO;
import DAO.ReclamoDAO;
import DAO.StockDAO;
import DAO.MovimientoInventarioDAO;
import DAO.UbicacionFisicaDAO;
import DAO.PermisoDAO;
import DAO.UsuarioDAO;

public class DAOFactory {
    public static ClienteDAO getClienteDAO() {
        return new ClienteDAOImpl();
    }

    public static ProductoDAO getProductoDAO() {
        return new ProductoDAOImpl();
    }

    public static PedidoDAO getPedidoDAO() {
        return new PedidoDAOImpl();
    }

    public static DetallePedidoDAO getDetallePedidoDAO() {
        return new DetallePedidoDAOImpl();
    }

    public static ReclamoDAO getReclamoDAO() {
        return new ReclamoDAOImpl();
    }

    public static StockDAO getStockDAO() {
        return new StockDAOImpl();
    }

    public static MovimientoInventarioDAO getMovimientoInventarioDAO() {
        return new MovimientoInventarioDAOImpl();
    }

    public static UbicacionFisicaDAO getUbicacionFisicaDAO() {
        return new UbicacionFisicaDAOImpl();
    }

    public static PermisoDAO getPermisoDAO() {
        return new PermisoDAOImpl();
    }

    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOImpl();
    }
}

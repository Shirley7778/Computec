package Servicio;

import DAO.ProductoDAO;
import DAO.impl.ProductoDAOImpl;
import Modelo.Producto;
import java.util.List;

public class ProductoServicio {
    private ProductoDAO dao = new ProductoDAOImpl();

    public void registrar(Producto p) throws Exception {
        dao.create(p);
    }

    public List<Producto> listar() throws Exception {
        return dao.findAll();
    }
}

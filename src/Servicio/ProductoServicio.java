package Servicio;

import DAO.ProductoDAO;
import DAO.impl.ProductoDAOImpl;
import Modelo.Producto;
import java.util.List;

public class ProductoServicio implements IProductoServicio {
    private ProductoDAO dao = new ProductoDAOImpl();

    @Override
    public void registrar(Producto p) throws Exception {
        dao.create(p);
    }

    @Override
    public List<Producto> listar() throws Exception {
        return dao.findAll();
    }
}

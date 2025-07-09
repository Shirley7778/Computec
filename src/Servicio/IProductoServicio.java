package Servicio;

import Modelo.Producto;
import java.util.List;

public interface IProductoServicio {
    void registrar(Producto p) throws Exception;
    List<Producto> listar() throws Exception;
}

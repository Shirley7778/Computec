package Servicio;

import Modelo.Cliente;
import Modelo.Producto;
import java.util.List;

public class SistemaFacade {
    private final ProductoServicio productoServicio = new ProductoServicio();
    private final ClienteServicio clienteServicio = new ClienteServicio();

    public void registrarProducto(Producto p) throws Exception {
        productoServicio.registrar(p);
    }

    public List<Producto> listarProductos() throws Exception {
        return productoServicio.listar();
    }

    public void registrarCliente(Cliente c) throws Exception {
        clienteServicio.registrar(c);
    }

    public List<Cliente> listarClientes() throws Exception {
        return clienteServicio.listar();
    }
}

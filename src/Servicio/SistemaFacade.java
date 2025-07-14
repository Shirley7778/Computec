package Servicio;

import DAO.DetallePedidoDAO;
import DAO.StockDAO;
import DAO.PedidoDAO;
import DAO.UsuarioDAO;
import DAO.DAOFactory;
import Modelo.Cliente;
import Modelo.DetallePedido;
import Modelo.Pedido;
import Modelo.Producto;
import Modelo.Stock;
import Modelo.Reclamo;
import Modelo.Usuario;
import java.util.List;

public class SistemaFacade {
    private final ProductoServicio productoServicio = new ProductoServicio();
    private final ClienteServicio clienteServicio = new ClienteServicio();
    private final PedidoServicio pedidoServicio = new PedidoServicio();
    private final ReclamoServicio reclamoServicio = new ReclamoServicio();

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
    

    
    public Cliente buscarClientePorRucDni(String rucDni) throws Exception {
        return clienteServicio.buscarPorRucDni(rucDni);
    }
    
    public void marcarClienteComoInactivo(long clienteId) throws Exception {
        clienteServicio.marcarComoInactivo(clienteId);
    }
    
    public void actualizarCliente(Cliente cliente) throws Exception {
        clienteServicio.actualizar(cliente);
    }
    
    public List<Pedido> listarPedidos() throws Exception {
        return pedidoServicio.listar();
    }
    
    public List<Pedido> obtenerPedidosCanceladosDelDia() throws Exception {
        return pedidoServicio.obtenerPedidosCanceladosDelDia();
    }
    
    public List<Pedido> obtenerPedidosConfirmadosYPendientes() throws Exception {
        return pedidoServicio.obtenerPedidosConfirmadosYPendientes();
    }
    


    // Métodos para Reclamos
    public void crearReclamo(Reclamo reclamo) throws Exception {
        reclamoServicio.crearReclamo(reclamo);
    }
    
    public void actualizarReclamo(Reclamo reclamo) throws Exception {
        reclamoServicio.actualizarReclamo(reclamo);
    }
    
    public List<Reclamo> listarReclamos() throws Exception {
        return reclamoServicio.listarReclamos();
    }
    
    public Reclamo obtenerReclamoPorId(long id) throws Exception {
        return reclamoServicio.obtenerReclamoPorId(id);
    }
    
    public List<Reclamo> obtenerReclamosPorRucDni(String rucDni) throws Exception {
        return reclamoServicio.obtenerReclamosPorRucDni(rucDni);
    }
    
    public void cerrarReclamo(long reclamoId) throws Exception {
        reclamoServicio.cerrarReclamo(reclamoId);
    }
    
    // Métodos para obtener datos relacionados con reclamos
    public List<Pedido> obtenerPedidosConfirmadosPorCliente(long clienteId) throws Exception {
        PedidoDAO pedidoDAO = DAOFactory.getPedidoDAO();
        return pedidoDAO.obtenerPedidosConfirmadosPorCliente(clienteId);
    }
    
    public List<Usuario> obtenerUsuariosSoporte() throws Exception {
        UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
        return usuarioDAO.obtenerUsuariosPorRol("soporte");
    }
    
    public Stock obtenerStockPorProductoId(long productoId) throws Exception {
        StockDAO stockDAO = new DAO.impl.StockDAOImpl();
        return stockDAO.findById(productoId);
    }

    public void actualizarStock(Stock stock) throws Exception {
        StockDAO stockDAO = new DAO.impl.StockDAOImpl();
        stockDAO.update(stock);
    }

    public void actualizarPedido(Pedido pedido) throws Exception {
        PedidoDAO pedidoDAO = DAOFactory.getPedidoDAO();
        pedidoDAO.update(pedido);
    }

    public String generarNumeroPedido() throws Exception {
        List<Pedido> pedidos = listarPedidos();
        long maxId = 0;
        for (Pedido p : pedidos) {
            if (p.getIdPedido() > maxId) maxId = p.getIdPedido();
        }
        return "PED-" + String.format("%04d", maxId + 1);
    }

    public void crearPedido(Pedido pedido, List<DetallePedido> detalles) throws Exception {
        PedidoDAO pedidoDAO = DAOFactory.getPedidoDAO();
        DetallePedidoDAO detalleDAO = DAOFactory.getDetallePedidoDAO();
        StockDAO stockDAO = DAOFactory.getStockDAO();
        
        // Crear el pedido
        pedidoDAO.create(pedido);
        
        // Crear los detalles y descontar stock
        for (DetallePedido detalle : detalles) {
            detalle.setPedido(pedido);
            detalleDAO.create(detalle);
            
            // Descontar stock
            Stock stock = stockDAO.findById(detalle.getProducto().getProductoId());
            if (stock != null) {
                stock.setCantidadActual(stock.getCantidadActual() - detalle.getCantidad());
                stockDAO.update(stock);
            }
        }
    }

    public List<DetallePedido> obtenerDetallesPorPedidoId(long pedidoId) throws Exception {
        DetallePedidoDAO detalleDAO = new DAO.impl.DetallePedidoDAOImpl();
        return detalleDAO.findByPedidoId(pedidoId);
    }

    public Cliente buscarClientePorRuc(String ruc) throws Exception {
        for (Cliente c : listarClientes()) {
            if (c.getRucDni().equalsIgnoreCase(ruc)) {
                return c;
            }
        }
        return null;
    }

    public Producto buscarProductoPorNombre(String nombre) throws Exception {
        for (Producto p : listarProductos()) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
}

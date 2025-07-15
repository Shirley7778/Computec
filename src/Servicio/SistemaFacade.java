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
import Modelo.UbicacionFisica;
import Modelo.Usuario;
import java.util.List;
import DAO.MovimientoInventarioDAO;
import Modelo.MovimientoInventario;

public class SistemaFacade {
    private final ProductoServicio productoServicio = new ProductoServicio();
    private final ClienteServicio clienteServicio = new ClienteServicio();
    private final PedidoServicio pedidoServicio = new PedidoServicio();
    private final ReclamoServicio reclamoServicio = new ReclamoServicio();
    private final UbicacionFisicaServicio ubicacionFisicaServicio = new UbicacionFisicaServicio();

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
    
    public List<Pedido> obtenerPedidosConfirmadosYPendientesDelDia() throws Exception {
        return pedidoServicio.obtenerPedidosConfirmadosYPendientesDelDia();
    }

    public List<Pedido> obtenerPedidosConfirmadosPorRangoFechas(java.time.LocalDate fechaIni, java.time.LocalDate fechaFin) throws Exception {
        return pedidoServicio.obtenerPedidosConfirmadosPorRangoFechas(fechaIni, fechaFin);
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
        DetallePedidoDAO detalleDAO = DAOFactory.getDetallePedidoDAO();
        StockDAO stockDAO = DAOFactory.getStockDAO();
        MovimientoInventarioDAO movDAO = DAOFactory.getMovimientoInventarioDAO();
        // Obtener el estado anterior del pedido
        Pedido pedidoOriginal = null;
        for (Pedido p : listarPedidos()) {
            if (p.getIdPedido() == pedido.getIdPedido()) {
                pedidoOriginal = p;
                break;
            }
        }
        String estadoAnterior = pedidoOriginal != null ? pedidoOriginal.getEstado() : null;
        // Actualizar el pedido en la base de datos
        pedidoDAO.update(pedido);
        // Si el estado cambió a Confirmado o Cancelado, crear movimientos
        if (estadoAnterior != null && !estadoAnterior.equals(pedido.getEstado())) {
            List<DetallePedido> detalles = detalleDAO.findByPedidoId(pedido.getIdPedido());
            for (DetallePedido detalle : detalles) {
                Stock stock = stockDAO.findById(detalle.getProducto().getProductoId());
                MovimientoInventario mov = new MovimientoInventario();
                mov.setPedido(pedido);
                mov.setStock(stock);
                mov.setReferencia("");
                mov.setObservaciones("");
                mov.setUbicacionProveedor("");
                mov.setUbicacionFisica(null);
                mov.setFecha(java.time.LocalDateTime.now());
                if ("Confirmado".equalsIgnoreCase(pedido.getEstado())) {
                    mov.setTipo("Salida");
                    mov.setMotivo("Venta");
                    mov.setCantidad(-detalle.getCantidad()); // Negativo
                } else if ("Cancelado".equalsIgnoreCase(pedido.getEstado())) {
                    mov.setTipo("Entrada");
                    mov.setMotivo("Venta Cancelada");
                    mov.setCantidad(detalle.getCantidad()); // Positivo
                } else {
                    continue;
                }
                movDAO.create(mov);
                // Actualizar stock solo si es cancelado (devolver stock)
                if ("Cancelado".equalsIgnoreCase(pedido.getEstado()) && stock != null) {
                    stock.setCantidadActual(stock.getCantidadActual() + detalle.getCantidad());
                    stockDAO.update(stock);
                }
            }
        }
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
    
    // Métodos para UbicacionFisica
    public void crearUbicacion(UbicacionFisica ubicacion) throws Exception {
        ubicacionFisicaServicio.crearUbicacion(ubicacion);
    }
    
    public void actualizarUbicacion(UbicacionFisica ubicacion) throws Exception {
        ubicacionFisicaServicio.actualizarUbicacion(ubicacion);
    }
    
    public void eliminarUbicacion(long ubicacionId) throws Exception {
        ubicacionFisicaServicio.eliminarUbicacion(ubicacionId);
    }
    
    public UbicacionFisica buscarUbicacionPorId(long ubicacionId) throws Exception {
        return ubicacionFisicaServicio.buscarUbicacionPorId(ubicacionId);
    }
    
    public UbicacionFisica buscarUbicacionPorNombre(String nombre) throws Exception {
        return ubicacionFisicaServicio.buscarUbicacionPorNombre(nombre);
    }
    
    public List<UbicacionFisica> listarUbicaciones() throws Exception {
        return ubicacionFisicaServicio.listarUbicaciones();
    }

    public void registrarProductoConStockYMovimiento(Producto producto, Stock stock, MovimientoInventario movimiento) throws Exception {
        DAO.ProductoDAO productoDAO = DAO.DAOFactory.getProductoDAO();
        // Si el producto no existe (id=0), crearlo
        if (producto.getProductoId() == 0) {
            productoDAO.create(producto);
            // Recuperar el ID generado
            if (productoDAO instanceof DAO.impl.ProductoDAOImpl) {
                Producto creado = ((DAO.impl.ProductoDAOImpl)productoDAO).findByNombre(producto.getNombre());
                producto.setProductoId(creado.getProductoId());
            }
        }
        // Registrar stock
        DAO.StockDAO stockDAO = DAO.DAOFactory.getStockDAO();
        stock.setProducto(producto);
        stockDAO.create(stock);
        // Recuperar el stock_id generado
        Stock stockCreado = stockDAO.findByProductoNombre(producto.getNombre());
        stock.setStockId(stockCreado.getStockId());
        // Registrar movimiento inventario
        DAO.MovimientoInventarioDAO movDAO = DAO.DAOFactory.getMovimientoInventarioDAO();
        movimiento.setStock(stock);
        movDAO.create(movimiento);
    }

    public java.util.List<Modelo.Stock> listarStock() throws Exception {
        DAO.StockDAO stockDAO = DAO.DAOFactory.getStockDAO();
        return stockDAO.findAll();
    }

    public List<Pedido> obtenerPedidosConfirmadosPorRucDni(String rucDni) throws Exception {
        Cliente cliente = clienteServicio.buscarPorRucDni(rucDni);
        if (cliente == null) return java.util.Collections.emptyList();
        return pedidoServicio.obtenerPedidosConfirmadosPorCliente(cliente.getIdCliente());
    }
}

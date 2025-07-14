package Servicio;

import DAO.PedidoDAO;
import DAO.DAOFactory;
import Modelo.Pedido;
import java.util.List;

/**
 * Servicio para manejar la lógica de negocio de los pedidos
 * Siguiendo el principio de Responsabilidad Única (SRP)
 */
public class PedidoServicio {
    private final PedidoDAO pedidoDAO;

    public PedidoServicio() {
        this.pedidoDAO = DAOFactory.getPedidoDAO();
    }

    public List<Pedido> listar() throws Exception {
        return pedidoDAO.findAll();
    }
    
    public List<Pedido> obtenerPedidosCanceladosDelDia() throws Exception {
        return pedidoDAO.obtenerPedidosCanceladosDelDia();
    }
    
    public List<Pedido> obtenerPedidosConfirmadosPorCliente(long clienteId) throws Exception {
        return pedidoDAO.obtenerPedidosConfirmadosPorCliente(clienteId);
    }
    
    public List<Pedido> obtenerPedidosConfirmadosYPendientes() throws Exception {
        List<Pedido> todosLosPedidos = pedidoDAO.findAll();
        return todosLosPedidos.stream()
            .filter(pedido -> "Confirmado".equals(pedido.getEstado()) || "Pendiente".equals(pedido.getEstado()))
            .collect(java.util.stream.Collectors.toList());
    }
} 
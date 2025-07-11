package Servicio;

import DAO.PedidoDAO;
import DAO.DAOFactory;
import Modelo.Pedido;
import java.util.List;

/**
 * Servicio para manejar la lógica de negocio de los pedidos cancelados
 */
public class PedidoServicio {
    private final PedidoDAO pedidoDAO;
    
    public PedidoServicio() {
        this.pedidoDAO = DAOFactory.getPedidoDAO();
    }
    
    /**
     * Obtiene los pedidos cancelados del día
     * @return Lista de pedidos cancelados del día
     * @throws Exception si ocurre un error en la base de datos
     */
    public List<Pedido> obtenerPedidosCanceladosDelDia() throws Exception {
        return pedidoDAO.obtenerPedidosCanceladosDelDia();
    }
} 
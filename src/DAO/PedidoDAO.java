package DAO;

import Modelo.Pedido;
import java.util.List;

public interface PedidoDAO {
    // Método para obtener pedidos cancelados del día
    List<Pedido> obtenerPedidosCanceladosDelDia() throws Exception;
}

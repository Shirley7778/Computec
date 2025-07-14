package DAO;

import Modelo.DetallePedido;
import java.util.List;

public interface DetallePedidoDAO extends Dao<DetallePedido> {
    List<DetallePedido> findByPedidoId(long pedidoId) throws Exception;
}

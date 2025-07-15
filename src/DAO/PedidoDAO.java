package DAO;

import Modelo.Pedido;
import java.util.List;

public interface PedidoDAO extends Dao<Pedido>  {
    List<Pedido> obtenerPedidosCanceladosDelDia() throws Exception;
    List<Pedido> obtenerPedidosConfirmadosPorCliente(long clienteId) throws Exception;
    List<Pedido> obtenerPedidosConfirmadosYPendientesDelDia() throws Exception;
    List<Pedido> obtenerPedidosConfirmadosPorRangoFechas(java.time.LocalDate fechaIni, java.time.LocalDate fechaFin) throws Exception;
}

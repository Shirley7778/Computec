package DAO.impl;

import DAO.PedidoDAO;
import Modelo.Pedido;
import Modelo.Cliente;
import ConexionBD.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {
    private final Conexion conexion = new Conexion();

    // Método para obtener pedidos cancelados del día
    @Override
    public List<Pedido> obtenerPedidosCanceladosDelDia() throws Exception {
        String sql = "SELECT p.*, c.* FROM pedidos p " +
                    "INNER JOIN clientes c ON p.cliente_id = c.id_cliente " +
                    "WHERE DATE(p.fecha) = CURDATE() AND p.estado = 'Cancelado' " +
                    "ORDER BY p.fecha DESC";
        
        List<Pedido> cancelados = new ArrayList<>();
        try (Connection conn = conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cancelados.add(mapearPedido(rs));
            }
        }
        return cancelados;
    }

    // Método auxiliar para mapear un ResultSet a un objeto Pedido
    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(
            rs.getLong("id_cliente"),
            rs.getString("ruc_dni"),
            rs.getString("tipo_empresa"),
            rs.getString("ciudad"),
            rs.getString("nombre_empresa"),
            rs.getString("codigo_postal"),
            rs.getString("direccion"),
            rs.getString("correo"),
            rs.getString("telefono_contacto"),
            rs.getString("personal_contacto")
        );
        return new Pedido(
            rs.getLong("id_pedido"),
            cliente,
            rs.getTimestamp("fecha").toLocalDateTime(),
            rs.getBigDecimal("subtotal"),
            rs.getBigDecimal("igv"),
            rs.getBigDecimal("total"),
            rs.getString("estado")
        );
    }
}

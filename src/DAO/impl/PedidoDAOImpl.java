package DAO.impl;

import DAO.PedidoDAO;
import Modelo.Pedido;
import ConexionBD.Conexion;
import Modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(Pedido p) throws Exception {
        String sql = "INSERT INTO pedidos (cliente_id, fecha, subtotal, igv, total, estado) VALUES (?, NOW(), ?, ?, ?, ?)";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, p.getCliente().getIdCliente());
            ps.setBigDecimal(2, p.getSubtotal());
            ps.setBigDecimal(3, p.getIgv());
            ps.setBigDecimal(4, p.getTotal());
            ps.setString(5, p.getEstado());
            ps.executeUpdate();
            try (java.sql.ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPedido(rs.getLong(1));
                }
            }
        }
        // Actualizar numero_pedido después de insertar
        String updateNumero = "UPDATE pedidos SET numero_pedido = CONCAT('PED-', LPAD(id_pedido, 3, '0')) WHERE id_pedido = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(updateNumero)) {
            ps.setLong(1, p.getIdPedido());
            ps.executeUpdate();
        }
    }

    @Override
    public Pedido findById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Pedido> findAll() throws Exception {
        String sql = "SELECT p.id_pedido, p.numero_pedido, p.fecha, p.subtotal, p.igv, p.total, p.estado, " +
                     "c.id_cliente, c.ruc_dni, c.tipo_empresa, c.ciudad, c.nombre_empresa, c.codigo_postal, c.direccion, c.correo, c.telefono_contacto, c.personal_contacto " +
                     "FROM pedidos p INNER JOIN clientes c ON p.cliente_id = c.id_cliente ORDER BY p.id_pedido DESC";
        List<Pedido> lista = new java.util.ArrayList<>();
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Modelo.Cliente cliente = new Modelo.Cliente(
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
                Modelo.Pedido pedido = new Modelo.Pedido();
                pedido.setIdPedido(rs.getLong("id_pedido"));
                pedido.setNumeroPedido(rs.getString("numero_pedido"));
                pedido.setCliente(cliente);
                pedido.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                pedido.setSubtotal(rs.getBigDecimal("subtotal"));
                pedido.setIgv(rs.getBigDecimal("igv"));
                pedido.setTotal(rs.getBigDecimal("total"));
                pedido.setEstado(rs.getString("estado"));
                lista.add(pedido);
            }
        }
        return lista;
    }

    @Override
    public void update(Pedido p) throws Exception {
        String sql = "UPDATE pedidos SET cliente_id = ?, subtotal = ?, igv = ?, total = ?, estado = ? WHERE id_pedido = ?";
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, p.getCliente().getIdCliente());
            ps.setBigDecimal(2, p.getSubtotal());
            ps.setBigDecimal(3, p.getIgv());
            ps.setBigDecimal(4, p.getTotal());
            ps.setString(5, p.getEstado());
            ps.setLong(6, p.getIdPedido());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
     // Método para obtener pedidos cancelados del día
    @Override
    public List<Pedido> obtenerPedidosCanceladosDelDia() throws Exception {
        String sql = "SELECT p.*, c.* FROM pedidos p " +
                    "INNER JOIN clientes c ON p.cliente_id = c.id_cliente " +
                    "WHERE DATE(p.fecha) = CURDATE() AND p.estado = 'Confirmado' " +
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

    @Override
    public List<Pedido> obtenerPedidosConfirmadosPorCliente(long clienteId) throws Exception {
        String sql = "SELECT p.id_pedido, p.numero_pedido, p.fecha, p.subtotal, p.igv, p.total, p.estado, " +
                     "c.id_cliente, c.ruc_dni, c.tipo_empresa, c.ciudad, c.nombre_empresa, c.codigo_postal, c.direccion, c.correo, c.telefono_contacto, c.personal_contacto " +
                     "FROM pedidos p INNER JOIN clientes c ON p.cliente_id = c.id_cliente " +
                     "WHERE p.cliente_id = ? AND p.estado = 'Confirmado' " +
                     "ORDER BY p.id_pedido DESC";
        List<Pedido> lista = new ArrayList<>();
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
                    Pedido pedido = new Pedido();
                    pedido.setIdPedido(rs.getLong("id_pedido"));
                    pedido.setNumeroPedido(rs.getString("numero_pedido"));
                    pedido.setCliente(cliente);
                    pedido.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    pedido.setSubtotal(rs.getBigDecimal("subtotal"));
                    pedido.setIgv(rs.getBigDecimal("igv"));
                    pedido.setTotal(rs.getBigDecimal("total"));
                    pedido.setEstado(rs.getString("estado"));
                    lista.add(pedido);
                }
            }
        }
        return lista;
    }

    @Override
    public List<Pedido> obtenerPedidosConfirmadosYPendientesDelDia() throws Exception {
        String sql = "SELECT p.id_pedido, p.numero_pedido, p.fecha, p.subtotal, p.igv, p.total, p.estado, " +
                     "c.id_cliente, c.ruc_dni, c.tipo_empresa, c.ciudad, c.nombre_empresa, c.codigo_postal, c.direccion, c.correo, c.telefono_contacto, c.personal_contacto " +
                     "FROM pedidos p INNER JOIN clientes c ON p.cliente_id = c.id_cliente " +
                     "WHERE (p.estado = 'Pendiente' OR p.estado = 'Confirmado') AND DATE(p.fecha) = CURDATE() " +
                     "ORDER BY p.id_pedido DESC";
        List<Pedido> lista = new java.util.ArrayList<>();
        try (java.sql.Connection cn = conexion.conectar();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Modelo.Cliente cliente = new Modelo.Cliente(
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
                Modelo.Pedido pedido = new Modelo.Pedido();
                pedido.setIdPedido(rs.getLong("id_pedido"));
                pedido.setNumeroPedido(rs.getString("numero_pedido"));
                pedido.setCliente(cliente);
                pedido.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                pedido.setSubtotal(rs.getBigDecimal("subtotal"));
                pedido.setIgv(rs.getBigDecimal("igv"));
                pedido.setTotal(rs.getBigDecimal("total"));
                pedido.setEstado(rs.getString("estado"));
                lista.add(pedido);
            }
        }
        return lista;
    }

    @Override
    public List<Pedido> obtenerPedidosConfirmadosPorRangoFechas(java.time.LocalDate fechaIni, java.time.LocalDate fechaFin) throws Exception {
        String sql = "SELECT p.id_pedido, p.numero_pedido, p.fecha, p.subtotal, p.igv, p.total, p.estado, " +
                     "c.id_cliente, c.ruc_dni, c.tipo_empresa, c.ciudad, c.nombre_empresa, c.codigo_postal, c.direccion, c.correo, c.telefono_contacto, c.personal_contacto " +
                     "FROM pedidos p INNER JOIN clientes c ON p.cliente_id = c.id_cliente " +
                     "WHERE p.estado = 'Confirmado' AND DATE(p.fecha) BETWEEN ? AND ? " +
                     "ORDER BY p.fecha DESC";
        List<Pedido> lista = new ArrayList<>();
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fechaIni));
            ps.setDate(2, java.sql.Date.valueOf(fechaFin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
                    Pedido pedido = new Pedido();
                    pedido.setIdPedido(rs.getLong("id_pedido"));
                    pedido.setNumeroPedido(rs.getString("numero_pedido"));
                    pedido.setCliente(cliente);
                    pedido.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    pedido.setSubtotal(rs.getBigDecimal("subtotal"));
                    pedido.setIgv(rs.getBigDecimal("igv"));
                    pedido.setTotal(rs.getBigDecimal("total"));
                    pedido.setEstado(rs.getString("estado"));
                    lista.add(pedido);
                }
            }
        }
        return lista;
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
            rs.getString("estado"),
            rs.getString("numero_pedido")
        );
    }
}

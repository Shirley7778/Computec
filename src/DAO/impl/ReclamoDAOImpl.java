package DAO.impl;

import DAO.ReclamoDAO;
import Modelo.Reclamo;
import Modelo.Cliente;
import Modelo.Pedido;
import Modelo.Usuario;
import ConexionBD.Conexion;
import java.sql.*;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ReclamoDAOImpl implements ReclamoDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(Reclamo r) throws Exception {
        String sql = "INSERT INTO reclamos (cliente_id, pedido_id, user_id, tipo, prioridad, estado, fecha_vencimiento, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, r.getCliente().getIdCliente());
            if (r.getPedido() != null) {
                ps.setLong(2, r.getPedido().getIdPedido());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setLong(3, r.getUsuario().getIdUsuario());
            ps.setString(4, r.getTipo());
            ps.setString(5, r.getPrioridad());
            ps.setString(6, r.getEstado());
            ps.setDate(7, r.getFechaVencimiento() != null ? Date.valueOf(r.getFechaVencimiento()) : null);
            ps.setString(8, r.getDescripcion());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    r.setReclamoId(rs.getLong(1));
                }
            }
        }
    }

    @Override
    public Reclamo findById(long id) throws Exception {
        String sql = "SELECT r.*, c.*, p.id_pedido, p.numero_pedido, p.fecha, p.subtotal, p.igv, p.total, p.estado as estado_pedido, u.* FROM reclamos r " +
                    "LEFT JOIN clientes c ON r.cliente_id = c.id_cliente " +
                    "LEFT JOIN pedidos p ON r.pedido_id = p.id_pedido " +
                    "LEFT JOIN usuarios u ON r.user_id = u.id_usuario " +
                    "WHERE r.reclamo_id = ?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearReclamo(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Reclamo> findAll() throws Exception {
        String sql = "SELECT r.*, c.*, p.id_pedido, p.numero_pedido, p.fecha, p.subtotal, p.igv, p.total, p.estado as estado_pedido, u.* FROM reclamos r " +
                    "LEFT JOIN clientes c ON r.cliente_id = c.id_cliente " +
                    "LEFT JOIN pedidos p ON r.pedido_id = p.id_pedido " +
                    "LEFT JOIN usuarios u ON r.user_id = u.id_usuario " +
                    "ORDER BY r.reclamo_id DESC";
        List<Reclamo> reclamos = new ArrayList<>();
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reclamos.add(mapearReclamo(rs));
            }
        }
        return reclamos;
    }

    @Override
    public void update(Reclamo r) throws Exception {
        String sql = "UPDATE reclamos SET cliente_id = ?, pedido_id = ?, user_id = ?, tipo = ?, prioridad = ?, estado = ?, fecha_vencimiento = ?, descripcion = ? WHERE reclamo_id = ?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, r.getCliente().getIdCliente());
            if (r.getPedido() != null) {
                ps.setLong(2, r.getPedido().getIdPedido());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setLong(3, r.getUsuario().getIdUsuario());
            ps.setString(4, r.getTipo());
            ps.setString(5, r.getPrioridad());
            ps.setString(6, r.getEstado());
            ps.setDate(7, r.getFechaVencimiento() != null ? Date.valueOf(r.getFechaVencimiento()) : null);
            ps.setString(8, r.getDescripcion());
            ps.setLong(9, r.getReclamoId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws Exception {
        String sql = "DELETE FROM reclamos WHERE reclamo_id = ?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Reclamo> findByClienteId(long clienteId) throws Exception {
        String sql = "SELECT r.*, c.*, p.id_pedido, p.numero_pedido, p.fecha, p.subtotal, p.igv, p.total, p.estado as estado_pedido, u.* FROM reclamos r " +
                    "LEFT JOIN clientes c ON r.cliente_id = c.id_cliente " +
                    "LEFT JOIN pedidos p ON r.pedido_id = p.id_pedido " +
                    "LEFT JOIN usuarios u ON r.user_id = u.id_usuario " +
                    "WHERE r.cliente_id = ? ORDER BY r.reclamo_id DESC";
        List<Reclamo> reclamos = new ArrayList<>();
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reclamos.add(mapearReclamo(rs));
                }
            }
        }
        return reclamos;
    }

    @Override
    public List<Reclamo> findByRucDni(String rucDni) throws Exception {
        String sql = "SELECT r.*, c.*, p.id_pedido, p.numero_pedido, p.fecha, p.subtotal, p.igv, p.total, p.estado as estado_pedido, u.* FROM reclamos r " +
                    "LEFT JOIN clientes c ON r.cliente_id = c.id_cliente " +
                    "LEFT JOIN pedidos p ON r.pedido_id = p.id_pedido " +
                    "LEFT JOIN usuarios u ON r.user_id = u.id_usuario " +
                    "WHERE c.ruc_dni = ? ORDER BY r.reclamo_id DESC";
        List<Reclamo> reclamos = new ArrayList<>();
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, rucDni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reclamos.add(mapearReclamo(rs));
                }
            }
        }
        return reclamos;
    }

    @Override
    public List<Reclamo> findByEstado(String estado) throws Exception {
        String sql = "SELECT r.*, c.*, p.id_pedido, p.numero_pedido, p.fecha, p.subtotal, p.igv, p.total, p.estado as estado_pedido, u.* FROM reclamos r " +
                    "LEFT JOIN clientes c ON r.cliente_id = c.id_cliente " +
                    "LEFT JOIN pedidos p ON r.pedido_id = p.id_pedido " +
                    "LEFT JOIN usuarios u ON r.user_id = u.id_usuario " +
                    "WHERE r.estado = ? ORDER BY r.reclamo_id DESC";
        List<Reclamo> reclamos = new ArrayList<>();
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reclamos.add(mapearReclamo(rs));
                }
            }
        }
        return reclamos;
    }

    @Override
    public void updateEstado(long reclamoId, String estado) throws Exception {
        String sql = "UPDATE reclamos SET estado = ? WHERE reclamo_id = ?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setLong(2, reclamoId);
            ps.executeUpdate();
        }
    }

    private Reclamo mapearReclamo(ResultSet rs) throws SQLException {
        // Mapear Cliente
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getLong("id_cliente"));
        cliente.setRucDni(rs.getString("ruc_dni"));
        cliente.setTipoEmpresa(rs.getString("tipo_empresa"));
        cliente.setCiudad(rs.getString("ciudad"));
        cliente.setNombreEmpresa(rs.getString("nombre_empresa"));
        cliente.setCodigoPostal(rs.getString("codigo_postal"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setTelefonoContacto(rs.getString("telefono_contacto"));
        cliente.setPersonalContacto(rs.getString("personal_contacto"));

        // Mapear Pedido (puede ser null)
        Pedido pedido = null;
        long pedidoId = rs.getLong("pedido_id");
        if (!rs.wasNull() && pedidoId != 0) {
            pedido = new Pedido();
            pedido.setIdPedido(pedidoId);
            pedido.setNumeroPedido(rs.getString("numero_pedido"));
            pedido.setCliente(cliente);
            pedido.setFecha(rs.getTimestamp("fecha") != null ? rs.getTimestamp("fecha").toLocalDateTime() : null);
            pedido.setSubtotal(rs.getBigDecimal("subtotal"));
            pedido.setIgv(rs.getBigDecimal("igv"));
            pedido.setTotal(rs.getBigDecimal("total"));
            pedido.setEstado(rs.getString("estado_pedido"));
        }

        // Mapear Usuario
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getLong("id_usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setDni(rs.getString("dni"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setContrasena(rs.getString("contrasena"));
        

        Reclamo reclamo = new Reclamo();
        reclamo.setReclamoId(rs.getLong("reclamo_id"));
        reclamo.setCliente(cliente);
        reclamo.setPedido(pedido);
        reclamo.setUsuario(usuario);
        reclamo.setTipo(rs.getString("tipo"));
        reclamo.setPrioridad(rs.getString("prioridad"));
        reclamo.setEstado(rs.getString("estado"));
        reclamo.setFechaVencimiento(rs.getDate("fecha_vencimiento") != null ? rs.getDate("fecha_vencimiento").toLocalDate() : null);
        reclamo.setDescripcion(rs.getString("descripcion"));
        return reclamo;
    }
}

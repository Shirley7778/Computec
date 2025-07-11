package DAO.impl;

import DAO.ClienteDAO;
import Modelo.Cliente;
import ConexionBD.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    private final Conexion conexion = new Conexion();

    @Override
    public void create(Cliente c) throws Exception {
        String sql = "INSERT INTO clientes(ruc_dni,tipo_empresa,ciudad,nombre_empresa,codigo_postal,direccion,correo,telefono_contacto,personal_contacto)" +
                     " VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getRucDni());
            ps.setString(2, c.getTipoEmpresa());
            ps.setString(3, c.getCiudad());
            ps.setString(4, c.getNombreEmpresa());
            ps.setString(5, c.getCodigoPostal());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getCorreo());
            ps.setString(8, c.getTelefonoContacto());
            ps.setString(9, c.getPersonalContacto());
            ps.executeUpdate();
        }
    }

    @Override
    public Cliente findById(long id) throws Exception {
        String sql = "SELECT * FROM clientes WHERE id_cliente=?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cliente c = map(rs);
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> findAll() throws Exception {
        String sql = "SELECT * FROM clientes WHERE estado = 'Activo'";
        List<Cliente> lista = new ArrayList<>();
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(map(rs));
            }
        }
        return lista;
    }

    @Override
    public void update(Cliente c) throws Exception {
        String sql = "UPDATE clientes SET ruc_dni=?,tipo_empresa=?,ciudad=?,nombre_empresa=?,codigo_postal=?,direccion=?,correo=?,telefono_contacto=?,personal_contacto=? WHERE id_cliente=?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getRucDni());
            ps.setString(2, c.getTipoEmpresa());
            ps.setString(3, c.getCiudad());
            ps.setString(4, c.getNombreEmpresa());
            ps.setString(5, c.getCodigoPostal());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getCorreo());
            ps.setString(8, c.getTelefonoContacto());
            ps.setString(9, c.getPersonalContacto());
            ps.setLong(10, c.getIdCliente());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws Exception {
        String sql = "DELETE FROM clientes WHERE id_cliente=?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Cliente findByRucDni(String rucDni) throws Exception {
        String sql = "SELECT * FROM clientes WHERE ruc_dni = ?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, rucDni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        }
        return null;
    }

    @Override
    public void setEstadoInactivo(long idCliente) throws Exception {
        String sql = "UPDATE clientes SET estado = 'Inactivo' WHERE id_cliente = ?";
        try (Connection cn = conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idCliente);
            ps.executeUpdate();
        }
    }

    private Cliente map(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getLong("id_cliente"));
        c.setRucDni(rs.getString("ruc_dni"));
        c.setTipoEmpresa(rs.getString("tipo_empresa"));
        c.setCiudad(rs.getString("ciudad"));
        c.setNombreEmpresa(rs.getString("nombre_empresa"));
        c.setCodigoPostal(rs.getString("codigo_postal"));
        c.setDireccion(rs.getString("direccion"));
        c.setCorreo(rs.getString("correo"));
        c.setTelefonoContacto(rs.getString("telefono_contacto"));
        c.setPersonalContacto(rs.getString("personal_contacto"));
        return c;
    }
}

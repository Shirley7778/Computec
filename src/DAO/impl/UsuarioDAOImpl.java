package DAO.impl;

import DAO.UsuarioDAO;
import Modelo.Usuario;
import Modelo.Permiso;
import ConexionBD.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    private final Conexion conexion = new Conexion();

    @Override
    public void create(Usuario u) throws Exception {
        String sql = "INSERT INTO usuarios (nombre, apellido, dni, telefono, contrasena, permiso_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getDni());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getContrasena());
            ps.setLong(6, u.getPermiso().getIdPermiso());
            
            ps.executeUpdate();
        }
    }

    @Override
    public Usuario findById(long id) throws Exception {
        String sql = "SELECT u.*, p.id_permiso, p.rol, p.acceso FROM usuarios u " +
                    "INNER JOIN permisos p ON u.permiso_id = p.id_permiso " +
                    "WHERE u.id_usuario = ?";
        
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }
        return null;
    }

    @Override
    public List<Usuario> findAll() throws Exception {
        String sql = "SELECT u.*, p.id_permiso, p.rol, p.acceso FROM usuarios u " +
                    "INNER JOIN permisos p ON u.permiso_id = p.id_permiso";
        
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }

    @Override
    public void update(Usuario u) throws Exception {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, dni = ?, telefono = ?, contrasena = ?, permiso_id = ? WHERE id_usuario = ?";
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getDni());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getContrasena());
            ps.setLong(6, u.getPermiso().getIdPermiso());
            ps.setLong(7, u.getIdUsuario());
            
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws Exception {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
    
    
    /**
     * Busca un usuario por nombre
     * @param nombre El nombre del usuario
     * @return El usuario encontrado o null si no existe
     */
    public Usuario findByNombre(String nombre) throws Exception {
        String sql = "SELECT u.*, p.id_permiso, p.rol, p.acceso FROM usuarios u " +
                    "INNER JOIN permisos p ON u.permiso_id = p.id_permiso " +
                    "WHERE u.nombre = ?";
        
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }
        return null;
    }
  
    @Override
    public void actualizarIntentosYUltimaVez(long idUsuario, int intentos, java.time.LocalDateTime ultimaVez) throws Exception {
        String sql = "UPDATE usuarios SET intentos = ?, ultima_vez = ? WHERE id_usuario = ?";
        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, intentos);
            if (ultimaVez != null) {
                ps.setTimestamp(2, java.sql.Timestamp.valueOf(ultimaVez));
            } else {
                ps.setNull(2, java.sql.Types.TIMESTAMP);
            }
            ps.setLong(3, idUsuario);
            ps.executeUpdate();
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Usuario
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getLong("id_usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setDni(rs.getString("dni"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setIntentos(rs.getInt("intentos"));
        Timestamp ts = rs.getTimestamp("ultima_vez");
        if (ts != null) {
            usuario.setUltimaVez(ts.toLocalDateTime());
        } else {
            usuario.setUltimaVez(null);
        }
        
        // Crear objeto Permiso
        Permiso permiso = new Permiso();
        permiso.setIdPermiso(rs.getLong("id_permiso"));
        permiso.setRol(rs.getString("rol"));
        permiso.setAcceso(rs.getString("acceso"));
        
        usuario.setPermiso(permiso);
        
        return usuario;
    }
}

package DAO;

import Modelo.Usuario;
import java.util.List;

public interface UsuarioDAO extends Dao<Usuario> {
    void actualizarIntentosYUltimaVez(long idUsuario, int intentos, java.time.LocalDateTime ultimaVez) throws Exception;
    List<Usuario> obtenerUsuariosPorRol(String rol) throws Exception;
}

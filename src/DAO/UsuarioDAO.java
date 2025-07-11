package DAO;

import Modelo.Usuario;

public interface UsuarioDAO extends Dao<Usuario> {
    void actualizarIntentosYUltimaVez(long idUsuario, int intentos, java.time.LocalDateTime ultimaVez) throws Exception;
}

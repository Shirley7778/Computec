package DAO;

import Modelo.Reclamo;
import java.util.List;

public interface ReclamoDAO extends Dao<Reclamo> {
    List<Reclamo> findByClienteId(long clienteId) throws Exception;
    List<Reclamo> findByRucDni(String rucDni) throws Exception;
    List<Reclamo> findByEstado(String estado) throws Exception;
    void updateEstado(long reclamoId, String estado) throws Exception;
}

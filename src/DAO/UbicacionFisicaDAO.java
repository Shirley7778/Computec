package DAO;

import Modelo.UbicacionFisica;

public interface UbicacionFisicaDAO extends Dao<UbicacionFisica> {
    UbicacionFisica findByNombre(String nombre) throws Exception;
}

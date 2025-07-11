package DAO;

import Modelo.Cliente;

public interface ClienteDAO extends Dao<Cliente> {
    Cliente findByRucDni(String rucDni) throws Exception;
    void setEstadoInactivo(long idCliente) throws Exception;
}

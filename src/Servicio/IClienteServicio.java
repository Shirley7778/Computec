package Servicio;

import Modelo.Cliente;
import java.util.List;

public interface IClienteServicio {
    void registrar(Cliente c) throws Exception;
    List<Cliente> listar() throws Exception;
}

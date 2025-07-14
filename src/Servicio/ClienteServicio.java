package Servicio;

import DAO.ClienteDAO;
import DAO.DAOFactory;
import Modelo.Cliente;
import java.util.List;

/**
 * Servicio para manejar la lógica de negocio de los clientes
 * Siguiendo el principio de Responsabilidad Única (SRP)
 */
public class ClienteServicio implements IClienteServicio {
    private final ClienteDAO clienteDAO;

    public ClienteServicio() {
        this.clienteDAO = DAOFactory.getClienteDAO();
    }

    @Override
    public void registrar(Cliente c) throws Exception {
        validarCliente(c);
        clienteDAO.create(c);
    }

    @Override
    public List<Cliente> listar() throws Exception {
        return clienteDAO.findAll();
    }
    
    public Cliente buscarPorRucDni(String rucDni) throws Exception {
        return clienteDAO.findByRucDni(rucDni);
    }
    
    public void marcarComoInactivo(long clienteId) throws Exception {
        clienteDAO.setEstadoInactivo(clienteId);
    }
    
    public void actualizar(Cliente cliente) throws Exception {
        validarCliente(cliente);
        clienteDAO.update(cliente);
    }
    
    /**
     * Validar datos del cliente antes de crear/actualizar
     */
    private void validarCliente(Cliente cliente) throws Exception {
        if (cliente.getRucDni() == null || cliente.getRucDni().trim().isEmpty()) {
            throw new Exception("El RUC/DNI es obligatorio");
        }
        if (cliente.getTipoEmpresa() == null || cliente.getTipoEmpresa().trim().isEmpty()) {
            throw new Exception("El tipo de empresa es obligatorio");
        }
        if (cliente.getCiudad() == null || cliente.getCiudad().trim().isEmpty()) {
            throw new Exception("La ciudad es obligatoria");
        }
    }
}

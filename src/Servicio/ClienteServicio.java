package Servicio;

import DAO.ClienteDAO;
import DAO.impl.ClienteDAOImpl;
import Modelo.Cliente;
import java.util.List;

public class ClienteServicio implements IClienteServicio {
    private ClienteDAO dao = new ClienteDAOImpl();

    @Override
    public void registrar(Cliente c) throws Exception {
        dao.create(c);
    }

    @Override
    public List<Cliente> listar() throws Exception {
        return dao.findAll();
    }
}

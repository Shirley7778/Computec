package Servicio;

import DAO.ClienteDAO;
import DAO.impl.ClienteDAOImpl;
import Modelo.Cliente;
import java.util.List;

public class ClienteServicio {
    private ClienteDAO dao = new ClienteDAOImpl();

    public void registrar(Cliente c) throws Exception {
        dao.create(c);
    }

    public List<Cliente> listar() throws Exception {
        return dao.findAll();
    }
}

package Servicio;

import DAO.StockDAO;
import DAO.impl.StockDAOImpl;
import Modelo.Stock;
import java.util.ArrayList;
import java.util.List;

public class StockServicio implements IStockServicio {
    private final List<InventarioObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(InventarioObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(InventarioObserver o) {
        observers.remove(o);
    }

    // Métodos que modifican el stock deberían llamar a notificar
    private void notificar() {
        observers.forEach(InventarioObserver::actualizado);
    }

    public void notificarObservers() {
        notificar();
    }

    // Método para descontar stock y notificar
    public void descontarStock(long productoId, int cantidad) {
        try {
            StockDAO stockDAO = new StockDAOImpl();
            Stock stock = stockDAO.findById(productoId);
            if (stock != null) {
                int nuevoStock = stock.getCantidadActual() - cantidad;
                stock.setCantidadActual(nuevoStock);
                stockDAO.update(stock);
                notificar();
            }
        } catch (Exception e) {
            // Manejo de error
        }
    }
}

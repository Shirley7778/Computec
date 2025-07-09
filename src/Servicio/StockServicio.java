package Servicio;

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
}

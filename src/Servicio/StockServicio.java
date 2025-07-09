package Servicio;

import java.util.ArrayList;
import java.util.List;

public class StockServicio {
    private final List<InventarioObserver> observers = new ArrayList<>();

    public void addObserver(InventarioObserver o) {
        observers.add(o);
    }

    public void removeObserver(InventarioObserver o) {
        observers.remove(o);
    }

    // Métodos que modifican el stock deberían llamar a notificar
    private void notificar() {
        observers.forEach(InventarioObserver::actualizado);
    }
}

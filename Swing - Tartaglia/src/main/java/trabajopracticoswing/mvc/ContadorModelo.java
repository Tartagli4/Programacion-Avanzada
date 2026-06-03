package trabajopracticoswing.mvc;

import java.util.ArrayList;
import java.util.List;

public class ContadorModelo {

    private int valor = 0;
    private final List<ContadorVista> vistas = new ArrayList<ContadorVista>();

    public int getValor() {
        return valor;
    }

    public void incrementar() {
        valor++;
        notificarVistas();
    }

    public void reiniciar() {
        valor = 0;
        notificarVistas();
    }

    public void agregarVista(ContadorVista vista) {
        vistas.add(vista);
        vista.actualizar(valor);
    }

    private void notificarVistas() {
        for (ContadorVista vista : vistas) {
            vista.actualizar(valor);
        }
    }
}

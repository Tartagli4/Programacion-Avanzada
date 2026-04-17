import java.util.ArrayList;
import java.util.List;

/**
 * PATRON DE COMPORTAMIENTO: Observer
 *
 * Define una dependencia uno-a-muchos entre objetos, de modo que cuando
 * un objeto cambia su estado, todos sus dependientes son notificados
 * y actualizados automaticamente.
 *
 * Caso de uso: estacion meteorologica que notifica a distintas
 * pantallas/displays cada vez que cambian los datos climaticos.
 */

// ── Interfaces ───────────────────────────────────────────────────────────────

interface Observable {
    void agregarObservador(Observador o);
    void eliminarObservador(Observador o);
    void notificarObservadores();
}

interface Observador {
    void actualizar(float temperatura, float humedad, float presion);
}

// ── Sujeto: la estacion meteorologica (Observable) ───────────────────────────

class EstacionMeteo implements Observable {

    private final List<Observador> observadores = new ArrayList<>();

    private float temperatura;
    private float humedad;
    private float presion;

    @Override
    public void agregarObservador(Observador o) {
        observadores.add(o);
        System.out.println("[Estacion] Observador registrado: "
                + o.getClass().getSimpleName());
    }

    @Override
    public void eliminarObservador(Observador o) {
        observadores.remove(o);
        System.out.println("[Estacion] Observador eliminado: "
                + o.getClass().getSimpleName());
    }

    @Override
    public void notificarObservadores() {
        for (Observador o : observadores) {
            o.actualizar(temperatura, humedad, presion);
        }
    }

    // Cuando cambian los datos, se notifica automaticamente a todos
    public void setMediciones(float temperatura, float humedad, float presion) {
        this.temperatura = temperatura;
        this.humedad     = humedad;
        this.presion     = presion;
        System.out.println("\n[Estacion] Nuevas mediciones recibidas. Notificando...");
        notificarObservadores();
    }
}

// ── Observadores: distintas pantallas que reaccionan al cambio ───────────────

class PantallaActual implements Observador {
    @Override
    public void actualizar(float temperatura, float humedad, float presion) {
        System.out.println("[Pantalla Actual]     Temp: " + temperatura
                + "°C | Humedad: " + humedad + "%");
    }
}

class PantallaEstadisticas implements Observador {
    private float tempMax = Float.MIN_VALUE;
    private float tempMin = Float.MAX_VALUE;

    @Override
    public void actualizar(float temperatura, float humedad, float presion) {
        if (temperatura > tempMax) tempMax = temperatura;
        if (temperatura < tempMin) tempMin = temperatura;
        System.out.println("[Pantalla Estadist.]  Temp max: " + tempMax
                + "°C | Temp min: " + tempMin + "°C");
    }
}

class PantallaPronostico implements Observador {
    @Override
    public void actualizar(float temperatura, float humedad, float presion) {
        String pronostico = presion > 1010 ? "Buen tiempo" : "Posible lluvia";
        System.out.println("[Pantalla Pronostico] Presion: " + presion
                + " hPa -> " + pronostico);
    }
}

// ── Demo ─────────────────────────────────────────────────────────────────────
public class EstacionMeteorologica {
    public static void main(String[] args) {
        System.out.println("=== PATRON OBSERVER ===\n");

        EstacionMeteo estacion = new EstacionMeteo();

        Observador pantallaActual  = new PantallaActual();
        Observador estadisticas    = new PantallaEstadisticas();
        Observador pronostico      = new PantallaPronostico();

        estacion.agregarObservador(pantallaActual);
        estacion.agregarObservador(estadisticas);
        estacion.agregarObservador(pronostico);

        // Primera medicion
        estacion.setMediciones(28.5f, 65.0f, 1013.0f);

        // Segunda medicion
        estacion.setMediciones(22.1f, 70.5f, 1005.0f);

        // Eliminar un observador y hacer una nueva medicion
        System.out.println();
        estacion.eliminarObservador(pronostico);
        estacion.setMediciones(19.0f, 80.0f, 998.0f);

        System.out.println("\nConclusion: cada cambio en la estacion notifica");
        System.out.println("automaticamente a todos los observadores registrados.");
    }
}

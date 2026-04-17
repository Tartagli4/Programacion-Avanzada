/**
 * PATRON DE CREACION: Singleton
 *
 * Garantiza que una clase tenga una unica instancia y provee
 * un punto de acceso global a ella.
 *
 * Caso de uso: configuracion global de la aplicacion.
 * Solo debe existir un objeto que gestione los parametros del sistema.
 */
public class ConfiguracionApp {

    // La unica instancia de la clase (se crea al cargar la clase - thread-safe)
    private static final ConfiguracionApp INSTANCIA = new ConfiguracionApp();

    // Parametros de configuracion
    private String host;
    private int puerto;
    private String modoApp;

    // Constructor privado: nadie puede instanciar la clase desde afuera
    private ConfiguracionApp() {
        // Valores por defecto
        this.host    = "localhost";
        this.puerto  = 8080;
        this.modoApp = "desarrollo";
        System.out.println("[Singleton] Instancia de ConfiguracionApp creada.");
    }

    // Punto de acceso global a la unica instancia
    public static ConfiguracionApp getInstancia() {
        return INSTANCIA;
    }

    // Getters y setters
    public String getHost()             { return host; }
    public void   setHost(String host)  { this.host = host; }

    public int  getPuerto()             { return puerto; }
    public void setPuerto(int puerto)   { this.puerto = puerto; }

    public String getModoApp()                  { return modoApp; }
    public void   setModoApp(String modoApp)    { this.modoApp = modoApp; }

    @Override
    public String toString() {
        return "ConfiguracionApp { host='" + host + "', puerto=" + puerto
                + ", modo='" + modoApp + "' }";
    }

    // ── Demo ────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== PATRON SINGLETON ===\n");

        // Primera referencia
        ConfiguracionApp config1 = ConfiguracionApp.getInstancia();
        System.out.println("config1: " + config1);

        // Modificar a traves de config1
        config1.setHost("192.168.1.100");
        config1.setPuerto(9090);
        config1.setModoApp("produccion");

        // Segunda referencia: apunta al MISMO objeto
        ConfiguracionApp config2 = ConfiguracionApp.getInstancia();
        System.out.println("config2: " + config2);

        // Verificar que son la misma instancia
        System.out.println("\n¿config1 == config2? " + (config1 == config2));
        System.out.println("Conclusion: ambas variables referencian el mismo objeto.");
    }
}

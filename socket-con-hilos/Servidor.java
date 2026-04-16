import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor multihilo con soporte de mensajeria entre clientes.
 * Puerto: 5000
 *
 * Comandos del cliente:
 *   AYUDA                  - muestra el menu de comandos
 *   FECHA                  - devuelve fecha y hora actual
 *   RESOLVE <expr>         - resuelve una expresion matematica
 *   LISTA                  - lista los clientes conectados
 *   *<usuario> <mensaje>   - envia mensaje a un cliente especifico
 *   *ALL <mensaje>         - envia mensaje a todos los clientes
 *   SALIR                  - desconecta al cliente
 */
public class Servidor {

    private static final int PUERTO = 5000;

    // Mapa compartido nombre -> handler (acceso seguro entre hilos)
    private static final ConcurrentHashMap<String, ClienteHandler> clientesConectados
            = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        log("SISTEMA", "Servidor iniciando en puerto " + PUERTO);
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            log("SISTEMA", "Esperando conexiones...");
            while (true) {
                Socket socket = serverSocket.accept();
                Thread hilo = new Thread(new ClienteHandler(socket));
                hilo.setDaemon(true);
                hilo.start();
            }
        } catch (IOException e) {
            log("ERROR", "Error al iniciar el servidor: " + e.getMessage());
        }
    }

    // ── Log con timestamp ────────────────────────────────────────────────────
    static void log(String origen, String mensaje) {
        String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + ts + "] [" + origen + "] " + mensaje);
    }

    // ── Asignar nombre unico ─────────────────────────────────────────────────
    static synchronized String asignarNombre(String nombreSolicitado) {
        String base = nombreSolicitado.trim().replaceAll("\\s+", "_");
        if (base.isEmpty()) base = "Cliente";
        if (!clientesConectados.containsKey(base)) return base;
        int n = 2;
        while (clientesConectados.containsKey(base + n)) n++;
        return base + n;
    }

    static void registrarCliente(String nombre, ClienteHandler handler) {
        clientesConectados.put(nombre, handler);
        log("SISTEMA", "Cliente registrado: " + nombre
                + " | Conectados: " + clientesConectados.size());
    }

    static void desregistrarCliente(String nombre) {
        clientesConectados.remove(nombre);
        log("SISTEMA", "Cliente desconectado: " + nombre
                + " | Conectados: " + clientesConectados.size());
        broadcast("SERVIDOR", nombre + " se ha desconectado.");
    }

    static void broadcast(String origen, String mensaje) {
        for (ClienteHandler c : clientesConectados.values()) {
            c.enviar("[" + origen + " -> TODOS] " + mensaje);
        }
    }

    static boolean enviarA(String destino, String origen, String mensaje) {
        ClienteHandler target = clientesConectados.get(destino);
        if (target == null) return false;
        target.enviar("[" + origen + " -> " + destino + "] " + mensaje);
        return true;
    }

    static String listaClientes() {
        if (clientesConectados.isEmpty()) return "No hay clientes conectados.";
        return "Conectados (" + clientesConectados.size() + "): "
                + String.join(", ", clientesConectados.keySet());
    }

    // ════════════════════════════════════════════════════════════════════════
    // Hilo por cliente
    // ════════════════════════════════════════════════════════════════════════
    static class ClienteHandler implements Runnable {

        private final Socket socket;
        private PrintWriter salida;
        private String nombre;

        ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        void enviar(String mensaje) {
            if (salida != null) salida.println(mensaje);
        }

        @Override
        public void run() {
            try (
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream()), true)
            ) {
                this.salida = out;

                // Pedir nombre
                salida.println("Ingresa tu nombre de usuario:");
                String nombreSolicitado = entrada.readLine();
                if (nombreSolicitado == null) return;

                nombre = asignarNombre(nombreSolicitado);
                registrarCliente(nombre, this);
                boolean renombrado = !nombre.equals(nombreSolicitado.trim());

                // Bienvenida
                salida.println("---");
                salida.println("Bienvenido, " + nombre + "!");
                if (renombrado) {
                    salida.println("Aviso: el nombre solicitado estaba en uso. Se te asigno: " + nombre);
                }
                salida.println(menuAyuda());
                salida.println("---");

                // Notificar a los demas
                for (Map.Entry<String, ClienteHandler> e : clientesConectados.entrySet()) {
                    if (!e.getKey().equals(nombre)) {
                        e.getValue().enviar("[SERVIDOR] " + nombre + " se ha conectado.");
                    }
                }

                // Bucle principal
                String linea;
                while ((linea = entrada.readLine()) != null) {
                    log(nombre, linea);
                    String respuesta = procesarComando(linea.trim());
                    if (respuesta.equals("__SALIR__")) {
                        salida.println("Hasta luego, " + nombre + "!");
                        break;
                    }
                    if (!respuesta.isEmpty()) salida.println(respuesta);
                }

            } catch (IOException e) {
                log("ERROR", "Conexion perdida con " + (nombre != null ? nombre : "desconocido"));
            } finally {
                if (nombre != null) desregistrarCliente(nombre);
                try { socket.close(); } catch (IOException ignored) {}
            }
        }

        private String procesarComando(String cmd) {
            if (cmd.equalsIgnoreCase("SALIR")) return "__SALIR__";

            if (cmd.equalsIgnoreCase("AYUDA") || cmd.equalsIgnoreCase("HELP"))
                return menuAyuda();

            if (cmd.equalsIgnoreCase("FECHA"))
                return "Fecha y hora: "
                        + new SimpleDateFormat("EEEE dd/MM/yyyy HH:mm:ss", new Locale("es"))
                                .format(new Date());

            if (cmd.toUpperCase().startsWith("RESOLVE "))
                return resolverExpresion(cmd.substring(8).trim());

            if (cmd.equalsIgnoreCase("LISTA"))
                return listaClientes();

            // *ALL <mensaje>
            if (cmd.toUpperCase().startsWith("*ALL ")) {
                String mensaje = cmd.substring(5).trim();
                if (mensaje.isEmpty()) return "ERROR: Escribe un mensaje. Ej: *ALL Hola a todos!";
                broadcast(nombre, mensaje);
                log(nombre, "Broadcast: " + mensaje);
                return "Mensaje enviado a todos.";
            }

            // *<destino> <mensaje>
            if (cmd.startsWith("*")) {
                int espacio = cmd.indexOf(' ');
                if (espacio == -1) return "ERROR: Formato: *<usuario> <mensaje>";
                String destino = cmd.substring(1, espacio).trim();
                String mensaje = cmd.substring(espacio + 1).trim();
                if (mensaje.isEmpty()) return "ERROR: El mensaje no puede estar vacio.";
                if (destino.equalsIgnoreCase(nombre))
                    return "ERROR: No puedes enviarte un mensaje a ti mismo.";
                boolean ok = enviarA(destino, nombre, mensaje);
                if (!ok) {
                    log("SERVIDOR", nombre + " intento enviar a '" + destino + "' (no existe).");
                    return "ERROR: El usuario '" + destino + "' no esta conectado. " + listaClientes();
                }
                log(nombre, "Mensaje privado a " + destino + ": " + mensaje);
                return "[ENVIADO -> " + destino + "] " + mensaje;
            }

            return "Comando no reconocido. Escribe AYUDA para ver los comandos.";
        }

        private String menuAyuda() {
            return "  COMANDOS DISPONIBLES\n"
                 + "  AYUDA                  - Muestra este menu\n"
                 + "  FECHA                  - Fecha y hora actual\n"
                 + "  RESOLVE <expr>         - Resuelve expresion matematica\n"
                 + "  LISTA                  - Lista de clientes conectados\n"
                 + "  *<usuario> <mensaje>   - Envia mensaje privado\n"
                 + "  *ALL <mensaje>         - Envia mensaje a todos\n"
                 + "  SALIR                  - Desconectarse";
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Evaluador de expresiones (parser recursivo descendente)
    // ════════════════════════════════════════════════════════════════════════
    private static int pos;
    private static String expr;

    static synchronized String resolverExpresion(String expresion) {
        if (expresion.isEmpty())
            return "ERROR: Escribe una expresion. Ej: RESOLVE 45*23/54+234";
        if (!expresion.matches("[0-9+\\-*/().\\s]+"))
            return "ERROR: Solo se permiten numeros y operadores ( + - * / parentesis ).";
        try {
            pos = 0;
            expr = expresion.replaceAll("\\s+", "");
            double resultado = parseExpresion();
            if (pos < expr.length())
                return "ERROR: Caracter inesperado en posicion " + pos;
            if (resultado == Math.floor(resultado) && !Double.isInfinite(resultado))
                return "Resultado de [" + expresion + "] = " + (long) resultado;
            return "Resultado de [" + expresion + "] = " + resultado;
        } catch (Exception e) {
            return "ERROR al evaluar: " + e.getMessage();
        }
    }

    private static double parseExpresion() {
        double r = parseTerm();
        while (pos < expr.length() && (expr.charAt(pos) == '+' || expr.charAt(pos) == '-')) {
            char op = expr.charAt(pos++);
            r = (op == '+') ? r + parseTerm() : r - parseTerm();
        }
        return r;
    }

    private static double parseTerm() {
        double r = parseFactor();
        while (pos < expr.length() && (expr.charAt(pos) == '*' || expr.charAt(pos) == '/')) {
            char op = expr.charAt(pos++);
            double f = parseFactor();
            if (op == '/') {
                if (f == 0) throw new RuntimeException("Division por cero");
                r /= f;
            } else r *= f;
        }
        return r;
    }

    private static double parseFactor() {
        if (pos < expr.length() && expr.charAt(pos) == '(') {
            pos++;
            double r = parseExpresion();
            if (pos < expr.length() && expr.charAt(pos) == ')') pos++;
            return r;
        }
        int inicio = pos;
        if (pos < expr.length() && expr.charAt(pos) == '-') pos++;
        while (pos < expr.length()
                && (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos) == '.')) pos++;
        if (inicio == pos) throw new RuntimeException("Numero esperado en posicion " + pos);
        return Double.parseDouble(expr.substring(inicio, pos));
    }
}

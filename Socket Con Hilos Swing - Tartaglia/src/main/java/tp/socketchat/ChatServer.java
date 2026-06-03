package tp.socketchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor de chat multihilo.
 * Cada cliente conectado es atendido por un hilo independiente.
 */
public class ChatServer {

    public interface ServerListener {
        void onLog(String mensaje);
        void onUsuariosActualizados(List<String> usuarios);
        void onServidorDetenido();
    }

    private final ConcurrentHashMap<String, ClienteHandler> clientesConectados = new ConcurrentHashMap<>();
    private final ServerListener listener;

    private ServerSocket serverSocket;
    private Thread hiloAceptador;
    private volatile boolean activo;

    public ChatServer(ServerListener listener) {
        this.listener = listener;
    }

    public boolean isActivo() {
        return activo;
    }

    public void iniciar(int puerto) throws IOException {
        if (activo) {
            throw new IllegalStateException("El servidor ya está iniciado.");
        }

        serverSocket = new ServerSocket(puerto);
        activo = true;
        log("SISTEMA", "Servidor iniciado en puerto " + puerto);

        hiloAceptador = new Thread(this::aceptarClientes, "Servidor-Aceptador");
        hiloAceptador.setDaemon(true);
        hiloAceptador.start();
    }

    public void detener() {
        activo = false;

        for (ClienteHandler cliente : clientesConectados.values()) {
            cliente.enviar("[SERVIDOR] El servidor se está cerrando.");
            cliente.cerrar();
        }
        clientesConectados.clear();
        notificarUsuarios();

        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }

        log("SISTEMA", "Servidor detenido.");
        if (listener != null) {
            listener.onServidorDetenido();
        }
    }

    private void aceptarClientes() {
        while (activo) {
            try {
                Socket socket = serverSocket.accept();
                Thread hiloCliente = new Thread(new ClienteHandler(socket), "Cliente-Handler");
                hiloCliente.setDaemon(true);
                hiloCliente.start();
            } catch (SocketException e) {
                if (activo) {
                    log("ERROR", "Error de socket: " + e.getMessage());
                }
            } catch (IOException e) {
                if (activo) {
                    log("ERROR", "No se pudo aceptar cliente: " + e.getMessage());
                }
            }
        }
    }

    private synchronized String asignarNombre(String nombreSolicitado) {
        String base = nombreSolicitado.trim();
        if (!clientesConectados.containsKey(base)) {
            return base;
        }

        int numero = 2;
        while (clientesConectados.containsKey(base + numero)) {
            numero++;
        }
        return base + numero;
    }

    private void registrarCliente(String nombre, ClienteHandler handler) {
        clientesConectados.put(nombre, handler);
        log("SISTEMA", "Cliente conectado: " + nombre + " | Total: " + clientesConectados.size());
        broadcast("SERVIDOR", nombre + " se ha conectado.", nombre);
        notificarUsuarios();
    }

    private void desregistrarCliente(String nombre) {
        if (nombre == null) {
            return;
        }

        clientesConectados.remove(nombre);
        log("SISTEMA", "Cliente desconectado: " + nombre + " | Total: " + clientesConectados.size());
        broadcast("SERVIDOR", nombre + " se ha desconectado.", nombre);
        notificarUsuarios();
    }

    private void broadcast(String origen, String mensaje, String usuarioExcluido) {
        for (Map.Entry<String, ClienteHandler> entry : clientesConectados.entrySet()) {
            if (usuarioExcluido != null && usuarioExcluido.equals(entry.getKey())) {
                continue;
            }
            entry.getValue().enviar("[" + origen + " -> TODOS] " + mensaje);
        }
    }

    private boolean enviarA(String destino, String origen, String mensaje) {
        ClienteHandler target = clientesConectados.get(destino);
        if (target == null) {
            return false;
        }

        target.enviar("[" + origen + " -> " + destino + "] " + mensaje);
        return true;
    }

    private String listaClientes() {
        if (clientesConectados.isEmpty()) {
            return "No hay clientes conectados.";
        }
        return "Conectados (" + clientesConectados.size() + "): "
                + String.join(", ", clientesConectados.keySet());
    }

    private void notificarUsuarios() {
        if (listener != null) {
            listener.onUsuariosActualizados(new ArrayList<>(clientesConectados.keySet()));
        }
    }

    private void log(String origen, String mensaje) {
        String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String linea = "[" + ts + "] [" + origen + "] " + mensaje;
        System.out.println(linea);
        if (listener != null) {
            listener.onLog(linea);
        }
    }

    private String menuAyuda() {
        return "COMANDOS DISPONIBLES\n"
                + "AYUDA                  - Muestra este menú\n"
                + "FECHA                  - Fecha y hora actual\n"
                + "RESOLVE <expr>         - Resuelve expresión matemática\n"
                + "LISTA                  - Lista clientes conectados\n"
                + "*<usuario> <mensaje>   - Mensaje privado\n"
                + "*ALL <mensaje>         - Mensaje a todos\n"
                + "SALIR                  - Desconectarse";
    }

    private class ClienteHandler implements Runnable {

        private final Socket socket;
        private PrintWriter salida;
        private String nombre;

        ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        void enviar(String mensaje) {
            if (salida != null) {
                salida.println(mensaje);
            }
        }

        void cerrar() {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }

        @Override
        public void run() {
            try (
                    BufferedReader entrada = new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
            ) {
                salida = out;
                salida.println("Ingresa tu nombre de usuario:");

                String nombreSolicitado = entrada.readLine();
                if (nombreSolicitado == null) {
                    return;
                }

                String errorUsuario = Validaciones.validarUsuario(nombreSolicitado);
                if (errorUsuario != null) {
                    salida.println("ERROR: " + errorUsuario);
                    salida.println("Conexión rechazada por usuario inválido.");
                    return;
                }

                nombre = asignarNombre(nombreSolicitado);
                registrarCliente(nombre, this);

                salida.println("---");
                salida.println("Bienvenido, " + nombre + "!");
                if (!nombre.equals(nombreSolicitado.trim())) {
                    salida.println("Aviso: el nombre solicitado estaba en uso. Se asignó: " + nombre);
                }
                salida.println(menuAyuda());
                salida.println("---");

                String linea;
                while ((linea = entrada.readLine()) != null) {
                    String comando = linea.trim();
                    if (comando.isEmpty()) {
                        salida.println("ERROR: El mensaje no puede estar vacío.");
                        continue;
                    }

                    log(nombre, comando);
                    String respuesta = procesarComando(comando);
                    if ("__SALIR__".equals(respuesta)) {
                        salida.println("Hasta luego, " + nombre + "!");
                        break;
                    }
                    if (!respuesta.isEmpty()) {
                        salida.println(respuesta);
                    }
                }
            } catch (IOException e) {
                if (activo) {
                    log("ERROR", "Conexión perdida con " + (nombre != null ? nombre : "cliente desconocido"));
                }
            } finally {
                desregistrarCliente(nombre);
                cerrar();
            }
        }

        private String procesarComando(String cmd) {
            if (cmd.equalsIgnoreCase("SALIR")) {
                return "__SALIR__";
            }

            if (cmd.equalsIgnoreCase("AYUDA") || cmd.equalsIgnoreCase("HELP")) {
                return menuAyuda();
            }

            if (cmd.equalsIgnoreCase("FECHA")) {
                return "Fecha y hora: "
                        + new SimpleDateFormat("EEEE dd/MM/yyyy HH:mm:ss", Locale.forLanguageTag("es-AR"))
                        .format(new Date());
            }

            if (cmd.toUpperCase().startsWith("RESOLVE ")) {
                return new EvaluadorExpresiones().resolver(cmd.substring(8).trim());
            }

            if (cmd.equalsIgnoreCase("LISTA")) {
                return listaClientes();
            }

            if (cmd.toUpperCase().startsWith("*ALL ")) {
                String mensaje = cmd.substring(5).trim();
                if (mensaje.isEmpty()) {
                    return "ERROR: Escribe un mensaje. Ej: *ALL Hola a todos!";
                }
                broadcast(nombre, mensaje, null);
                return "Mensaje enviado a todos.";
            }

            if (cmd.startsWith("*")) {
                int espacio = cmd.indexOf(' ');
                if (espacio == -1) {
                    return "ERROR: Formato correcto: *<usuario> <mensaje>";
                }

                String destino = cmd.substring(1, espacio).trim();
                String mensaje = cmd.substring(espacio + 1).trim();

                if (mensaje.isEmpty()) {
                    return "ERROR: El mensaje no puede estar vacío.";
                }

                if (destino.equalsIgnoreCase(nombre)) {
                    return "ERROR: No puedes enviarte un mensaje a ti mismo.";
                }

                boolean enviado = enviarA(destino, nombre, mensaje);
                if (!enviado) {
                    return "ERROR: El usuario '" + destino + "' no está conectado. " + listaClientes();
                }
                return "[ENVIADO -> " + destino + "] " + mensaje;
            }

            return "Comando no reconocido. Escribe AYUDA para ver los comandos.";
        }
    }

    private static class EvaluadorExpresiones {

        private int pos;
        private String expr;

        String resolver(String expresion) {
            if (expresion.isEmpty()) {
                return "ERROR: Escribe una expresión. Ej: RESOLVE 45*23/54+234";
            }

            if (!expresion.matches("[0-9+\\-*/().\\s]+")) {
                return "ERROR: Solo se permiten números y operadores + - * / paréntesis.";
            }

            try {
                pos = 0;
                expr = expresion.replaceAll("\\s+", "");
                double resultado = parseExpresion();

                if (pos < expr.length()) {
                    return "ERROR: Carácter inesperado en posición " + pos;
                }

                if (resultado == Math.floor(resultado) && !Double.isInfinite(resultado)) {
                    return "Resultado de [" + expresion + "] = " + (long) resultado;
                }

                return "Resultado de [" + expresion + "] = " + resultado;
            } catch (Exception e) {
                return "ERROR al evaluar: " + e.getMessage();
            }
        }

        private double parseExpresion() {
            double r = parseTerm();
            while (pos < expr.length() && (expr.charAt(pos) == '+' || expr.charAt(pos) == '-')) {
                char op = expr.charAt(pos++);
                r = (op == '+') ? r + parseTerm() : r - parseTerm();
            }
            return r;
        }

        private double parseTerm() {
            double r = parseFactor();
            while (pos < expr.length() && (expr.charAt(pos) == '*' || expr.charAt(pos) == '/')) {
                char op = expr.charAt(pos++);
                double f = parseFactor();
                if (op == '/') {
                    if (f == 0) {
                        throw new RuntimeException("División por cero");
                    }
                    r /= f;
                } else {
                    r *= f;
                }
            }
            return r;
        }

        private double parseFactor() {
            if (pos < expr.length() && expr.charAt(pos) == '(') {
                pos++;
                double r = parseExpresion();
                if (pos < expr.length() && expr.charAt(pos) == ')') {
                    pos++;
                } else {
                    throw new RuntimeException("Falta cerrar paréntesis");
                }
                return r;
            }

            int inicio = pos;
            if (pos < expr.length() && expr.charAt(pos) == '-') {
                pos++;
            }

            while (pos < expr.length()
                    && (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos) == '.')) {
                pos++;
            }

            if (inicio == pos) {
                throw new RuntimeException("Número esperado en posición " + pos);
            }

            return Double.parseDouble(expr.substring(inicio, pos));
        }
    }
}

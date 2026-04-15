import java.io.*;
import java.net.*;
import java.util.Date;

public class Servidor {

    private static final int PUERTO = 5000;
    private static final String MENSAJE_SALIDA = "SALIR";

    public static void main(String[] args) {
        System.out.println("[SERVIDOR] Iniciando en puerto " + PUERTO + "...");
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("[SERVIDOR] Esperando conexiones...\n");
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("[SERVIDOR] Cliente conectado desde: "
                        + socketCliente.getInetAddress().getHostAddress()
                        + " | " + new Date());
                atenderCliente(socketCliente);
                System.out.println("[SERVIDOR] Cliente desconectado. Esperando nueva conexion...\n");
            }
        } catch (IOException e) {
            System.err.println("[SERVIDOR] Error: " + e.getMessage());
        }
    }

    private static void atenderCliente(Socket socketCliente) {
        try (
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socketCliente.getInputStream()));
            PrintWriter salida = new PrintWriter(
                    new OutputStreamWriter(socketCliente.getOutputStream()), true)
        ) {
            salida.println("Bienvenido al Servidor! Puedes:");
            salida.println("  - Escribir cualquier mensaje de chat");
            salida.println("  - Usar RESOLVE <expresion> para calcular (ej: RESOLVE 45*23/54+234)");
            salida.println("  - Escribir SALIR para desconectarte");
            salida.println("---");

            String mensajeRecibido;
            while ((mensajeRecibido = entrada.readLine()) != null) {
                System.out.println("[LOG " + new Date() + "] Cliente dice: " + mensajeRecibido);

                if (mensajeRecibido.trim().equalsIgnoreCase(MENSAJE_SALIDA)) {
                    salida.println("Hasta luego! Cerrando conexion...");
                    System.out.println("[SERVIDOR] El cliente envio el mensaje de salida.");
                    break;
                }

                String respuesta = procesarMensaje(mensajeRecibido);
                salida.println(respuesta);
                System.out.println("[LOG " + new Date() + "] Respuesta enviada: " + respuesta);
                System.out.println();
            }
        } catch (IOException e) {
            System.err.println("[SERVIDOR] Error de comunicacion: " + e.getMessage());
        } finally {
            try { socketCliente.close(); } catch (IOException e) {}
        }
    }

    private static String procesarMensaje(String mensaje) {
        String m = mensaje.trim();
        if (m.toUpperCase().startsWith("RESOLVE")) {
            String expresion = m.substring(7).trim();
            if (expresion.isEmpty()) return "ERROR: Escribi una expresion. Ej: RESOLVE 45*23/54+234";
            return resolverExpresion(expresion);
        }
        if (m.equalsIgnoreCase("AYUDA") || m.equalsIgnoreCase("HELP")) {
            return "Comandos: RESOLVE <expr> | AYUDA | SALIR";
        }
        return "Servidor recibio tu mensaje: \"" + m + "\"";
    }

    private static String resolverExpresion(String expresion) {
        try {
            if (!expresion.matches("[0-9+\\-*/().\\s]+")) {
                return "ERROR: Solo se permiten numeros y operadores ( + - * / )";
            }
            double resultado = evaluar(expresion.replaceAll("\\s+", ""));
            if (resultado == Math.floor(resultado) && !Double.isInfinite(resultado)) {
                return "Resultado de [" + expresion + "] = " + (long) resultado;
            }
            return "Resultado de [" + expresion + "] = " + resultado;
        } catch (Exception e) {
            return "ERROR al evaluar: " + e.getMessage();
        }
    }

    // --- Evaluador de expresiones con precedencia correcta ---
    private static int pos;
    private static String expr;

    private static double evaluar(String expresion) {
        pos = 0;
        expr = expresion;
        double resultado = parseExpresion();
        if (pos < expr.length()) throw new RuntimeException("Caracter inesperado: " + expr.charAt(pos));
        return resultado;
    }

    private static double parseExpresion() {
        double resultado = parseTerm();
        while (pos < expr.length() && (expr.charAt(pos) == '+' || expr.charAt(pos) == '-')) {
            char op = expr.charAt(pos++);
            double term = parseTerm();
            resultado = (op == '+') ? resultado + term : resultado - term;
        }
        return resultado;
    }

    private static double parseTerm() {
        double resultado = parseFactor();
        while (pos < expr.length() && (expr.charAt(pos) == '*' || expr.charAt(pos) == '/')) {
            char op = expr.charAt(pos++);
            double factor = parseFactor();
            if (op == '/') {
                if (factor == 0) throw new RuntimeException("Division por cero");
                resultado /= factor;
            } else {
                resultado *= factor;
            }
        }
        return resultado;
    }

    private static double parseFactor() {
        if (pos < expr.length() && expr.charAt(pos) == '(') {
            pos++; // consumir '('
            double resultado = parseExpresion();
            if (pos < expr.length() && expr.charAt(pos) == ')') pos++; // consumir ')'
            return resultado;
        }
        int inicio = pos;
        if (pos < expr.length() && expr.charAt(pos) == '-') pos++;
        while (pos < expr.length() && (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos) == '.')) pos++;
        return Double.parseDouble(expr.substring(inicio, pos));
    }
}
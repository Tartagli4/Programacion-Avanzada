import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Cliente de chat/calc con soporte multihilo.
 * Se conecta al servidor en localhost:5000.
 *
 * Al conectarse se solicita un nombre de usuario.
 * Comandos disponibles: ver AYUDA en el servidor.
 */
public class Cliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        System.out.println("[CLIENTE] Conectando a " + HOST + ":" + PUERTO + "...");

        try (
            Socket socket = new Socket(HOST, PUERTO);
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()), true);
            Scanner teclado = new Scanner(System.in)
        ) {
            System.out.println("[CLIENTE] Conexion establecida.\n");

            // Hilo lector: muestra mensajes del servidor en tiempo real
            Thread lector = new Thread(() -> {
                try {
                    String linea;
                    while ((linea = entrada.readLine()) != null) {
                        System.out.println("\n[SERVIDOR] " + linea);
                        System.out.print("> ");
                    }
                } catch (IOException e) {
                    System.out.println("\n[CLIENTE] Conexion cerrada por el servidor.");
                }
            });
            lector.setDaemon(true);
            lector.start();

            // Hilo principal: enviar mensajes
            String mensaje;
            while (true) {
                System.out.print("> ");
                mensaje = teclado.nextLine();
                if (mensaje.trim().isEmpty()) continue;
                salida.println(mensaje);
                if (mensaje.trim().equalsIgnoreCase("SALIR")) {
                    Thread.sleep(400);
                    break;
                }
            }

            System.out.println("[CLIENTE] Desconectado.");

        } catch (ConnectException e) {
            System.err.println("[CLIENTE] No se pudo conectar. Asegurate de que el Servidor este corriendo.");
        } catch (IOException e) {
            System.err.println("[CLIENTE] Error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

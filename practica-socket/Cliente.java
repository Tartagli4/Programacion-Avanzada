import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;
    private static final String MENSAJE_SALIDA = "SALIR";

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║         CLIENTE DE CHAT/CALC         ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("[CLIENTE] Conectando a " + HOST + ":" + PUERTO + "...\n");

        try (
            Socket socket = new Socket(HOST, PUERTO);
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()), true);
            Scanner teclado = new Scanner(System.in)
        ) {
            System.out.println("[CLIENTE] Conectado al servidor!\n");

            String lineaBienvenida;
            while ((lineaBienvenida = entrada.readLine()) != null) {
                System.out.println("[SERVIDOR] " + lineaBienvenida);
                if (lineaBienvenida.equals("---")) break;
            }

            System.out.println();

            Thread hiloLector = new Thread(() -> {
                try {
                    String respuesta;
                    while ((respuesta = entrada.readLine()) != null) {
                        System.out.println("\n[SERVIDOR] " + respuesta);
                        System.out.print("[TU] > ");
                    }
                } catch (IOException e) {
                    System.out.println("\n[CLIENTE] Conexion con el servidor cerrada.");
                }
            });

            hiloLector.setDaemon(true);
            hiloLector.start();

            String mensajeUsuario;
            while (true) {
                System.out.print("[TU] > ");
                mensajeUsuario = teclado.nextLine();

                if (mensajeUsuario.trim().isEmpty()) {
                    continue;
                }

                salida.println(mensajeUsuario);

                if (mensajeUsuario.trim().equalsIgnoreCase(MENSAJE_SALIDA)) {
                    System.out.println("[CLIENTE] Enviando mensaje de salida...");
                    Thread.sleep(500);
                    break;
                }
            }

            System.out.println("[CLIENTE] Desconectado. Hasta luego!");

        } catch (ConnectException e) {
            System.err.println("[CLIENTE] No se pudo conectar al servidor.");
            System.err.println("          Asegurate de que Servidor.java este ejecutandose en el puerto " + PUERTO + ".");
        } catch (IOException e) {
            System.err.println("[CLIENTE] Error de conexion: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
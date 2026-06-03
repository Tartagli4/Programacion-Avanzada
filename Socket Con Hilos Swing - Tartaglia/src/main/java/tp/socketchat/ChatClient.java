package tp.socketchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Cliente de chat usado por la interfaz Swing.
 */
public class ChatClient {

    public interface ClientListener {
        void onMensajeRecibido(String mensaje);
        void onEstado(String estado);
        void onDesconectado();
        void onError(String error);
    }

    private final ClientListener listener;

    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private Thread hiloLector;
    private volatile boolean conectado;

    public ChatClient(ClientListener listener) {
        this.listener = listener;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void conectar(String host, int puerto, String usuario) throws IOException {
        if (conectado) {
            throw new IllegalStateException("El cliente ya está conectado.");
        }

        socket = new Socket(host, puerto);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        conectado = true;

        hiloLector = new Thread(this::leerMensajes, "Cliente-Lector");
        hiloLector.setDaemon(true);
        hiloLector.start();

        salida.println(usuario.trim());
        notificarEstado("Conectado a " + host + ":" + puerto + " como " + usuario.trim());
    }

    public void enviar(String mensaje) {
        if (!conectado || salida == null) {
            notificarError("No hay conexión activa.");
            return;
        }
        salida.println(mensaje);
    }

    public void desconectar() {
        if (!conectado) {
            return;
        }

        try {
            if (salida != null) {
                salida.println("SALIR");
            }
        } catch (Exception ignored) {
        }

        cerrarRecursos();
    }

    private void leerMensajes() {
        try {
            String linea;
            while (conectado && (linea = entrada.readLine()) != null) {
                if (listener != null) {
                    listener.onMensajeRecibido(linea);
                }
            }
        } catch (IOException e) {
            if (conectado) {
                notificarError("Se perdió la conexión con el servidor.");
            }
        } finally {
            cerrarRecursos();
        }
    }

    private void cerrarRecursos() {
        boolean estabaConectado = conectado;
        conectado = false;

        try {
            if (entrada != null) {
                entrada.close();
            }
        } catch (IOException ignored) {
        }

        if (salida != null) {
            salida.close();
        }

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }

        if (estabaConectado && listener != null) {
            listener.onDesconectado();
        }
    }

    private void notificarEstado(String estado) {
        if (listener != null) {
            listener.onEstado(estado);
        }
    }

    private void notificarError(String error) {
        if (listener != null) {
            listener.onError(error);
        }
    }
}

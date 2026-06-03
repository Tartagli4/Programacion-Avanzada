package tp.socketchat;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Lanzador general del trabajo práctico.
 * Permite abrir la interfaz del servidor o la del cliente desde una sola clase main.
 */
public class AppLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] opciones = {"Servidor", "Cliente"};
            int seleccion = JOptionPane.showOptionDialog(
                    null,
                    "Selecciona qué ventana deseas abrir:",
                    "Socket con Hilos + Swing",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion == 0) {
                new ServidorFrame().setVisible(true);
            } else if (seleccion == 1) {
                new ClienteFrame().setVisible(true);
            }
        });
    }
}

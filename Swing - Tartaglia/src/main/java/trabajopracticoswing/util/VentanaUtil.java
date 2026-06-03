package trabajopracticoswing.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public final class VentanaUtil {

    private VentanaUtil() {
    }

    public static void prepararVentana(JFrame ventana, int ancho, int alto) {
        ventana.setSize(ancho, alto);
        ventana.setMinimumSize(new Dimension(Math.min(ancho, 420), Math.min(alto, 260)));
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void centrar(JFrame ventana) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - ventana.getWidth()) / 2;
        int y = (screen.height - ventana.getHeight()) / 2;
        ventana.setLocation(Math.max(x, 0), Math.max(y, 0));
    }
}

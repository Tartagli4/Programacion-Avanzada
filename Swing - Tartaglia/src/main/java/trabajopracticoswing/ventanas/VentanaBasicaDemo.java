package trabajopracticoswing.ventanas;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import trabajopracticoswing.util.VentanaUtil;

public class VentanaBasicaDemo extends JFrame {

    public VentanaBasicaDemo() {
        super("Ventana basica");
        VentanaUtil.prepararVentana(this, 420, 260);
        crearContenido();
    }

    private void crearContenido() {
        JLabel texto = new JLabel("Ejemplo basico de JFrame", SwingConstants.CENTER);
        texto.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        add(texto, BorderLayout.CENTER);
    }
}

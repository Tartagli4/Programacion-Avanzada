package trabajopracticoswing.ventanas;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import trabajopracticoswing.util.VentanaUtil;

public class VentanaExtendidaDemo extends JFrame {

    public VentanaExtendidaDemo() {
        super("Ventana extendida desde JFrame");
        configurarVentana();
        crearContenido();
    }

    private void configurarVentana() {
        VentanaUtil.prepararVentana(this, 460, 280);
    }

    private void crearContenido() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel titulo = new JLabel("Clase propia que extiende JFrame", SwingConstants.CENTER);
        JLabel detalle = new JLabel("La inicializacion se realiza dentro del constructor.", SwingConstants.CENTER);

        panel.add(titulo, BorderLayout.CENTER);
        panel.add(detalle, BorderLayout.SOUTH);
        add(panel);
    }
}

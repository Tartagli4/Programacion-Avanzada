package trabajopracticoswing.layouts;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import trabajopracticoswing.util.VentanaUtil;

public class FlowLayoutDemo extends JFrame {

    public FlowLayoutDemo() {
        super("FlowLayout");
        VentanaUtil.prepararVentana(this, 460, 240);
        crearContenido();
    }

    private void crearContenido() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 16));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.add(new JLabel("Botones colocados uno a continuacion de otro:"));
        panel.add(new JButton("Boton 1"));
        panel.add(new JButton("Boton 2"));
        panel.add(new JButton("Boton 3"));
        panel.add(new JButton("Boton 4"));
        add(panel);
    }
}

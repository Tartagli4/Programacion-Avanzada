package trabajopracticoswing.layouts;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import trabajopracticoswing.util.VentanaUtil;

public class BorderLayoutDemo extends JFrame {

    public BorderLayoutDemo() {
        super("BorderLayout");
        VentanaUtil.prepararVentana(this, 520, 320);
        crearContenido();
    }

    private void crearContenido() {
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(new JButton("NORTH"), BorderLayout.NORTH);
        add(new JButton("SOUTH"), BorderLayout.SOUTH);
        add(new JButton("WEST"), BorderLayout.WEST);
        add(new JButton("EAST"), BorderLayout.EAST);
        add(new JLabel("CENTER: zona principal de la ventana", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}

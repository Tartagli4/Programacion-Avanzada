package trabajopracticoswing.graficos;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import trabajopracticoswing.util.VentanaUtil;

public class VentanaSolDemo extends JFrame {

    public VentanaSolDemo() {
        super("Dibujo de graficos - Sol");
        VentanaUtil.prepararVentana(this, 520, 420);
        add(new PanelSol(), BorderLayout.CENTER);
    }
}

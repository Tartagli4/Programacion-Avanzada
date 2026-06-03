package trabajopracticoswing.mvc;

import javax.swing.JFrame;
import trabajopracticoswing.util.VentanaUtil;

public class ContadorMVCDemo extends JFrame {

    public ContadorMVCDemo() {
        super("Arquitectura MVC - Contador");
        VentanaUtil.prepararVentana(this, 420, 260);

        ContadorModelo modelo = new ContadorModelo();
        ContadorVista vista = new ContadorVista();
        modelo.agregarVista(vista);
        new ContadorControlador(modelo, vista);

        add(vista);
    }
}

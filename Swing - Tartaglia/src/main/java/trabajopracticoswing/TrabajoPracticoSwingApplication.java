package trabajopracticoswing;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TrabajoPracticoSwingApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                configurarLookAndFeel();
                new MenuPrincipal().setVisible(true);
            }
        });
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println("No se pudo aplicar el estilo del sistema. Se usara el estilo por defecto.");
        }
    }
}

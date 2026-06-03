package edu.uda.tpformulario;

import edu.uda.tpformulario.ui.ContactoFormFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Si no se puede aplicar el estilo del sistema, Swing usa el estilo por defecto.
            }

            ContactoFormFrame formulario = new ContactoFormFrame();
            formulario.setVisible(true);
        });
    }
}

package tp.socketchat;

import javax.swing.SwingUtilities;

public class AppServidor {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServidorFrame().setVisible(true));
    }
}

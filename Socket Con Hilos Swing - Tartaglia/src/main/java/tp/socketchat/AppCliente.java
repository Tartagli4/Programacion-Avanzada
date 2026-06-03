package tp.socketchat;

import javax.swing.SwingUtilities;

public class AppCliente {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClienteFrame().setVisible(true));
    }
}

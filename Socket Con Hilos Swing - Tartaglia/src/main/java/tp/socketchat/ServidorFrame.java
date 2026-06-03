package tp.socketchat;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Interfaz gráfica del servidor.
 */
public class ServidorFrame extends JFrame {

    private final JTextField txtPuerto = new JTextField("5000", 8);
    private final JButton btnIniciar = new JButton("Iniciar servidor");
    private final JButton btnDetener = new JButton("Detener servidor");
    private final JLabel lblEstado = new JLabel("Servidor detenido");
    private final JTextArea areaLog = new JTextArea();
    private final DefaultListModel<String> modeloUsuarios = new DefaultListModel<>();
    private final JList<String> listaUsuarios = new JList<>(modeloUsuarios);

    private ChatServer servidor;

    public ServidorFrame() {
        super("Servidor Chat - Socket con Hilos y Swing");
        configurarVentana();
        configurarFiltros();
        armarInterfaz();
        agregarEventos();
        actualizarEstado(false);
    }

    private void configurarVentana() {
        setSize(760, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void configurarFiltros() {
        ((AbstractDocument) txtPuerto.getDocument()).setDocumentFilter(new SoloNumerosFilter(5));
    }

    private void armarInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(12, 12, 12, 12));

        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        panelPrincipal.add(crearPanelUsuarios(), BorderLayout.EAST);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Puerto:"), gbc);

        gbc.gridx = 1;
        panel.add(txtPuerto, gbc);

        gbc.gridx = 2;
        panel.add(btnIniciar, gbc);

        gbc.gridx = 3;
        panel.add(btnDetener, gbc);

        gbc.gridx = 4;
        gbc.weightx = 1;
        panel.add(lblEstado, gbc);

        return panel;
    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(new EmptyBorder(0, 8, 0, 0));
        panel.add(new JLabel("Usuarios conectados"), BorderLayout.NORTH);
        panel.add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);
        return panel;
    }

    private void agregarEventos() {
        btnIniciar.addActionListener(e -> iniciarServidor());
        btnDetener.addActionListener(e -> detenerServidor());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                detenerServidor();
            }
        });
    }

    private void iniciarServidor() {
        String errorPuerto = Validaciones.validarPuerto(txtPuerto.getText());
        if (errorPuerto != null) {
            JOptionPane.showMessageDialog(this, errorPuerto, "Validación", JOptionPane.WARNING_MESSAGE);
            txtPuerto.requestFocus();
            return;
        }

        int puerto = Validaciones.parsearPuerto(txtPuerto.getText());

        servidor = new ChatServer(new ChatServer.ServerListener() {
            @Override
            public void onLog(String mensaje) {
                SwingUtilities.invokeLater(() -> agregarLog(mensaje));
            }

            @Override
            public void onUsuariosActualizados(List<String> usuarios) {
                SwingUtilities.invokeLater(() -> actualizarUsuarios(usuarios));
            }

            @Override
            public void onServidorDetenido() {
                SwingUtilities.invokeLater(() -> actualizarEstado(false));
            }
        });

        try {
            servidor.iniciar(puerto);
            actualizarEstado(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo iniciar el servidor: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            actualizarEstado(false);
        }
    }

    private void detenerServidor() {
        if (servidor != null && servidor.isActivo()) {
            servidor.detener();
        }
        actualizarEstado(false);
    }

    private void actualizarEstado(boolean activo) {
        btnIniciar.setEnabled(!activo);
        btnDetener.setEnabled(activo);
        txtPuerto.setEnabled(!activo);
        lblEstado.setText(activo ? "Servidor iniciado" : "Servidor detenido");
    }

    private void agregarLog(String linea) {
        areaLog.append(linea + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private void actualizarUsuarios(List<String> usuarios) {
        modeloUsuarios.clear();
        usuarios.sort(String::compareToIgnoreCase);
        for (String usuario : usuarios) {
            modeloUsuarios.addElement(usuario);
        }
    }

    private static class SoloNumerosFilter extends DocumentFilter {

        private final int maximo;

        SoloNumerosFilter(int maximo) {
            this.maximo = maximo;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) {
                return;
            }

            String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevo = actual.substring(0, offset) + text + actual.substring(offset + length);

            if (nuevo.length() <= maximo && nuevo.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}

package tp.socketchat;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
 * Interfaz gráfica del cliente de chat.
 */
public class ClienteFrame extends JFrame {

    private final JTextField txtHost = new JTextField("localhost", 12);
    private final JTextField txtPuerto = new JTextField("5000", 6);
    private final JTextField txtUsuario = new JTextField(12);
    private final JTextArea areaChat = new JTextArea();
    private final JTextField txtMensaje = new JTextField();
    private final JLabel lblEstado = new JLabel("Desconectado");

    private final JButton btnConectar = new JButton("Conectar");
    private final JButton btnDesconectar = new JButton("Desconectar");
    private final JButton btnEnviar = new JButton("Enviar");
    private final JButton btnAyuda = new JButton("Ayuda");
    private final JButton btnLista = new JButton("Lista");
    private final JButton btnFecha = new JButton("Fecha");

    private ChatClient cliente;

    public ClienteFrame() {
        super("Cliente Chat - Socket con Hilos y Swing");
        configurarVentana();
        configurarFiltros();
        armarInterfaz();
        agregarEventos();
        actualizarEstadoConexion(false);
    }

    private void configurarVentana() {
        setSize(760, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void configurarFiltros() {
        ((AbstractDocument) txtPuerto.getDocument()).setDocumentFilter(new SoloNumerosFilter(5));
        ((AbstractDocument) txtUsuario.getDocument()).setDocumentFilter(new UsuarioFilter());
        ((AbstractDocument) txtMensaje.getDocument()).setDocumentFilter(new LimiteCaracteresFilter(300));
    }

    private void armarInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(12, 12, 12, 12));

        areaChat.setEditable(false);
        areaChat.setLineWrap(true);
        areaChat.setWrapStyleWord(true);
        areaChat.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        panelPrincipal.add(crearPanelConexion(), BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(areaChat), BorderLayout.CENTER);
        panelPrincipal.add(crearPanelInferior(), BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelConexion() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Host:"), gbc);

        gbc.gridx = 1;
        panel.add(txtHost, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Puerto:"), gbc);

        gbc.gridx = 3;
        panel.add(txtPuerto, gbc);

        gbc.gridx = 4;
        panel.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 5;
        gbc.weightx = 1;
        panel.add(txtUsuario, gbc);

        gbc.weightx = 0;
        gbc.gridx = 6;
        panel.add(btnConectar, gbc);

        gbc.gridx = 7;
        panel.add(btnDesconectar, gbc);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));

        JPanel panelMensaje = new JPanel(new BorderLayout(6, 6));
        panelMensaje.add(new JLabel("Mensaje / comando:"), BorderLayout.WEST);
        panelMensaje.add(txtMensaje, BorderLayout.CENTER);
        panelMensaje.add(btnEnviar, BorderLayout.EAST);

        JPanel panelComandos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelComandos.add(btnAyuda);
        panelComandos.add(btnLista);
        panelComandos.add(btnFecha);
        panelComandos.add(lblEstado);

        contenedor.add(panelMensaje, BorderLayout.NORTH);
        contenedor.add(panelComandos, BorderLayout.SOUTH);
        return contenedor;
    }

    private void agregarEventos() {
        btnConectar.addActionListener(e -> conectar());
        btnDesconectar.addActionListener(e -> desconectar());
        btnEnviar.addActionListener(e -> enviarMensaje());
        txtMensaje.addActionListener(e -> enviarMensaje());

        btnAyuda.addActionListener(e -> enviarComandoRapido("AYUDA"));
        btnLista.addActionListener(e -> enviarComandoRapido("LISTA"));
        btnFecha.addActionListener(e -> enviarComandoRapido("FECHA"));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                desconectar();
            }
        });
    }

    private void conectar() {
        String errorHost = Validaciones.validarHost(txtHost.getText());
        String errorPuerto = Validaciones.validarPuerto(txtPuerto.getText());
        String errorUsuario = Validaciones.validarUsuario(txtUsuario.getText());

        if (errorHost != null) {
            mostrarError(errorHost);
            txtHost.requestFocus();
            return;
        }
        if (errorPuerto != null) {
            mostrarError(errorPuerto);
            txtPuerto.requestFocus();
            return;
        }
        if (errorUsuario != null) {
            mostrarError(errorUsuario);
            txtUsuario.requestFocus();
            return;
        }

        String host = txtHost.getText().trim();
        int puerto = Validaciones.parsearPuerto(txtPuerto.getText());
        String usuario = txtUsuario.getText().trim();

        cliente = new ChatClient(new ChatClient.ClientListener() {
            @Override
            public void onMensajeRecibido(String mensaje) {
                SwingUtilities.invokeLater(() -> agregarLinea(mensaje));
            }

            @Override
            public void onEstado(String estado) {
                SwingUtilities.invokeLater(() -> lblEstado.setText(estado));
            }

            @Override
            public void onDesconectado() {
                SwingUtilities.invokeLater(() -> {
                    agregarLinea("[CLIENTE] Desconectado.");
                    actualizarEstadoConexion(false);
                });
            }

            @Override
            public void onError(String error) {
                SwingUtilities.invokeLater(() -> {
                    agregarLinea("[ERROR] " + error);
                    lblEstado.setText(error);
                });
            }
        });

        btnConectar.setEnabled(false);
        lblEstado.setText("Conectando...");

        new Thread(() -> {
            try {
                cliente.conectar(host, puerto, usuario);
                SwingUtilities.invokeLater(() -> {
                    agregarLinea("[CLIENTE] Conexión establecida.");
                    actualizarEstadoConexion(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    mostrarError("No se pudo conectar: " + ex.getMessage());
                    actualizarEstadoConexion(false);
                });
            }
        }, "Cliente-Conectar").start();
    }

    private void desconectar() {
        if (cliente != null && cliente.isConectado()) {
            cliente.desconectar();
        }
        actualizarEstadoConexion(false);
    }

    private void enviarMensaje() {
        String mensaje = txtMensaje.getText();
        String errorMensaje = Validaciones.validarMensaje(mensaje);

        if (errorMensaje != null) {
            mostrarError(errorMensaje);
            txtMensaje.requestFocus();
            return;
        }

        enviarComandoRapido(mensaje.trim());
        txtMensaje.setText("");
    }

    private void enviarComandoRapido(String comando) {
        if (cliente == null || !cliente.isConectado()) {
            mostrarError("Primero debes conectarte al servidor.");
            return;
        }

        cliente.enviar(comando);
        agregarLinea("[YO] " + comando);
    }

    private void actualizarEstadoConexion(boolean conectado) {
        txtHost.setEnabled(!conectado);
        txtPuerto.setEnabled(!conectado);
        txtUsuario.setEnabled(!conectado);
        btnConectar.setEnabled(!conectado);
        btnDesconectar.setEnabled(conectado);
        btnEnviar.setEnabled(conectado);
        btnAyuda.setEnabled(conectado);
        btnLista.setEnabled(conectado);
        btnFecha.setEnabled(conectado);
        txtMensaje.setEnabled(conectado);
        lblEstado.setText(conectado ? "Conectado" : "Desconectado");
    }

    private void agregarLinea(String linea) {
        areaChat.append(linea + "\n");
        areaChat.setCaretPosition(areaChat.getDocument().getLength());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Validación", JOptionPane.WARNING_MESSAGE);
    }

    private static class UsuarioFilter extends DocumentFilter {

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

            if (nuevo.length() <= 15 && nuevo.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ0-9_-]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
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

    private static class LimiteCaracteresFilter extends DocumentFilter {

        private final int maximo;

        LimiteCaracteresFilter(int maximo) {
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

            if (nuevo.length() <= maximo) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}

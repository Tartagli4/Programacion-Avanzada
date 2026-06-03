package edu.uda.tpformulario.ui;

import edu.uda.tpformulario.util.FormValidator;
import edu.uda.tpformulario.util.RegexDocumentFilter;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

public class ContactoFormFrame extends JFrame {
    private final JTextField nombreField = new JTextField(20);
    private final JTextField apellidoField = new JTextField(20);
    private final JTextField dniField = new JTextField(10);
    private final JTextField pasaporteField = new JTextField(10);
    private final JTextField telefonoField = new JTextField(20);
    private final JTextField codigoPostalField = new JTextField(8);
    private final JTextField domicilioField = new JTextField(30);
    private final JLabel estadoLabel = new JLabel("Complete los datos del contacto.");

    public ContactoFormFrame() {
        configurarVentana();
        configurarFiltros();
        configurarEventosDocumento();
        cargarComponentes();
    }

    private void configurarVentana() {
        setTitle("Carga de contacto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void configurarFiltros() {
        ((AbstractDocument) nombreField.getDocument()).setDocumentFilter(
                new RegexDocumentFilter("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*", 20));
        ((AbstractDocument) apellidoField.getDocument()).setDocumentFilter(
                new RegexDocumentFilter("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*", 20));
        ((AbstractDocument) dniField.getDocument()).setDocumentFilter(
                new RegexDocumentFilter("\\d*", 8));
        ((AbstractDocument) pasaporteField.getDocument()).setDocumentFilter(
                new RegexDocumentFilter("([A-Z]?|[A-Z]\\d{0,8})", 9, true));
        ((AbstractDocument) telefonoField.getDocument()).setDocumentFilter(
                new RegexDocumentFilter("[0-9+()\\- ]*", 25));
        ((AbstractDocument) codigoPostalField.getDocument()).setDocumentFilter(
                new RegexDocumentFilter("\\d*", 4));
        ((AbstractDocument) domicilioField.getDocument()).setDocumentFilter(
                new RegexDocumentFilter(".*", 50));
    }

    private void configurarEventosDocumento() {
        DocumentListener listenerDocumento = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarEstadoDocumento();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarEstadoDocumento();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarEstadoDocumento();
            }
        };

        dniField.getDocument().addDocumentListener(listenerDocumento);
        pasaporteField.getDocument().addDocumentListener(listenerDocumento);
    }

    private void cargarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel titulo = new JLabel("Carga de contacto", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(18f));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = crearPanelFormulario();
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(crearPanelBotones(), BorderLayout.SOUTH);

        add(panelPrincipal);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        agregarCampo(panel, gbc, 0, "Nombre:", nombreField, "Máximo 20 caracteres alfabéticos.");
        agregarCampo(panel, gbc, 1, "Apellido:", apellidoField, "Máximo 20 caracteres alfabéticos.");
        agregarCampo(panel, gbc, 2, "DNI:", dniField, "8 dígitos entre 10.000.000 y 60.000.000.");
        agregarCampo(panel, gbc, 3, "Pasaporte:", pasaporteField, "Ejemplo: N39392288.");
        agregarCampo(panel, gbc, 4, "Teléfono:", telefonoField, "Más de 6 dígitos. Se permite +()-.");
        agregarCampo(panel, gbc, 5, "Código Postal:", codigoPostalField, "4 dígitos numéricos.");
        agregarCampo(panel, gbc, 6, "Domicilio:", domicilioField, "Máximo 50 caracteres.");

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        estadoLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
        panel.add(estadoLabel, gbc);

        return panel;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta,
                              JTextField campo, String ayuda) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        campo.setToolTipText(ayuda);
        panel.add(campo, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        JLabel ayudaLabel = new JLabel(ayuda);
        ayudaLabel.setFont(ayudaLabel.getFont().deriveFont(11f));
        panel.add(ayudaLabel, gbc);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();

        JButton validarButton = new JButton("Validar");
        JButton limpiarButton = new JButton("Limpiar");
        JButton cerrarButton = new JButton("Cerrar");

        validarButton.addActionListener(e -> validarFormulario());
        limpiarButton.addActionListener(e -> limpiarFormulario());
        cerrarButton.addActionListener(e -> dispose());

        panel.add(validarButton);
        panel.add(limpiarButton);
        panel.add(cerrarButton);

        return panel;
    }

    private void actualizarEstadoDocumento() {
        boolean tieneDni = !dniField.getText().isBlank();
        boolean tienePasaporte = !pasaporteField.getText().isBlank();

        pasaporteField.setEnabled(!tieneDni);
        dniField.setEnabled(!tienePasaporte);

        if (tieneDni) {
            estadoLabel.setText("DNI cargado. El campo Pasaporte queda deshabilitado.");
        } else if (tienePasaporte) {
            estadoLabel.setText("Pasaporte cargado. El campo DNI queda deshabilitado.");
        } else {
            estadoLabel.setText("Complete los datos del contacto.");
        }
    }

    private void validarFormulario() {
        String nombre = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();
        String dni = dniField.getText().trim();
        String pasaporte = pasaporteField.getText().trim();
        String telefono = telefonoField.getText().trim();
        String codigoPostal = codigoPostalField.getText().trim();
        String domicilio = domicilioField.getText().trim();

        List<String> errores = FormValidator.validarFormulario(
                nombre, apellido, dni, pasaporte, telefono, codigoPostal, domicilio);

        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    String.join("\n", errores),
                    "Errores de validación",
                    JOptionPane.ERROR_MESSAGE
            );
            estadoLabel.setText("Existen errores pendientes de corrección.");
            return;
        }

        String documento = !dni.isBlank() ? "DNI: " + dni : "Pasaporte: " + pasaporte;
        String mensaje = "Contacto validado correctamente.\n\n"
                + "Nombre: " + nombre + " " + apellido + "\n"
                + documento + "\n"
                + "Teléfono: " + telefono + "\n"
                + "Código Postal: " + codigoPostal + "\n"
                + "Domicilio: " + domicilio;

        JOptionPane.showMessageDialog(this, mensaje, "Carga correcta", JOptionPane.INFORMATION_MESSAGE);
        estadoLabel.setText("Formulario validado correctamente.");
    }

    private void limpiarFormulario() {
        nombreField.setText("");
        apellidoField.setText("");
        dniField.setText("");
        pasaporteField.setText("");
        telefonoField.setText("");
        codigoPostalField.setText("");
        domicilioField.setText("");
        dniField.setEnabled(true);
        pasaporteField.setEnabled(true);
        estadoLabel.setText("Complete los datos del contacto.");
        nombreField.requestFocusInWindow();
    }
}

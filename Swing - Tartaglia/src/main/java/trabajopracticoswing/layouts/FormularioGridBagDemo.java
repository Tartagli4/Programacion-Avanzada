package trabajopracticoswing.layouts;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import trabajopracticoswing.util.VentanaUtil;

public class FormularioGridBagDemo extends JFrame {

    private JTextField nombreField;
    private JTextField emailField;
    private JComboBox<String> carreraCombo;

    public FormularioGridBagDemo() {
        super("GridBagLayout - Formulario avanzado");
        VentanaUtil.prepararVentana(this, 500, 300);
        crearContenido();
    }

    private void crearContenido() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));

        nombreField = new JTextField(20);
        emailField = new JTextField(20);
        carreraCombo = new JComboBox<String>(new String[]{"Lic. en Informatica", "Desarrollo de Software", "Otra carrera"});

        agregarFila(panel, 0, "Nombre:", nombreField);
        agregarFila(panel, 1, "Email:", emailField);
        agregarFila(panel, 2, "Carrera:", carreraCombo);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardar = new JButton("Guardar");
        guardar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { guardar(); }
        });
        panelBotones.add(guardar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarFila(JPanel panel, int fila, String etiqueta, java.awt.Component componente) {
        GridBagConstraints gbcEtiqueta = new GridBagConstraints();
        gbcEtiqueta.gridx = 0;
        gbcEtiqueta.gridy = fila;
        gbcEtiqueta.anchor = GridBagConstraints.EAST;
        gbcEtiqueta.insets = new Insets(6, 6, 6, 10);
        panel.add(new JLabel(etiqueta), gbcEtiqueta);

        GridBagConstraints gbcCampo = new GridBagConstraints();
        gbcCampo.gridx = 1;
        gbcCampo.gridy = fila;
        gbcCampo.weightx = 1.0;
        gbcCampo.fill = GridBagConstraints.HORIZONTAL;
        gbcCampo.insets = new Insets(6, 6, 6, 6);
        panel.add(componente, gbcCampo);
    }

    private void guardar() {
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        if (nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y email son obligatorios.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Datos guardados para: " + nombre, "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
    }
}

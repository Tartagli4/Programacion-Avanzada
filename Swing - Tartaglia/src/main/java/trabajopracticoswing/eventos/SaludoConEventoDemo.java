package trabajopracticoswing.eventos;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import trabajopracticoswing.util.VentanaUtil;

public class SaludoConEventoDemo extends JFrame implements ActionListener {

    private JTextField nombreField;

    public SaludoConEventoDemo() {
        super("Saludo con ActionListener");
        VentanaUtil.prepararVentana(this, 460, 180);
        crearContenido();
    }

    private void crearContenido() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 24));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        nombreField = new JTextField(20);
        JButton botonSaludo = new JButton("Saludar");
        botonSaludo.addActionListener(this);

        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(botonSaludo);
        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nombre = nombreField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre antes de saludar.", "Dato requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Hola, " + nombre + "!", "Saludo", JOptionPane.INFORMATION_MESSAGE);
    }
}

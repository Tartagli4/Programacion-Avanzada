package trabajopracticoswing.layouts;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

public class FormularioUsuarioPanelesDemo extends JFrame {

    private JTextField nombreField;
    private JTextField dniField;
    private JTextField diaField;
    private JTextField mesField;
    private JTextField anioField;

    public FormularioUsuarioPanelesDemo() {
        super("JPanel - Anadir usuario");
        VentanaUtil.prepararVentana(this, 480, 260);
        crearContenido();
    }

    private void crearContenido() {
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        diaField = new JTextField(2);
        mesField = new JTextField(2);
        anioField = new JTextField(4);
        panelFecha.add(diaField);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(mesField);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(anioField);

        JPanel panelDatos = new JPanel(new GridLayout(3, 2, 8, 8));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        nombreField = new JTextField(12);
        dniField = new JTextField(12);

        panelDatos.add(new JLabel("Nombre:"));
        panelDatos.add(nombreField);
        panelDatos.add(new JLabel("DNI:"));
        panelDatos.add(dniField);
        panelDatos.add(new JLabel("Fecha de nacimiento:"));
        panelDatos.add(panelFecha);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton aceptar = new JButton("Aceptar");
        JButton cancelar = new JButton("Cancelar");

        aceptar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { aceptar(); }
        });
        cancelar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { limpiar(); }
        });

        panelBotones.add(aceptar);
        panelBotones.add(cancelar);

        add(panelDatos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void aceptar() {
        if (nombreField.getText().trim().isEmpty() || dniField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete nombre y DNI.", "Formulario incompleto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Usuario cargado correctamente.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiar() {
        nombreField.setText("");
        dniField.setText("");
        diaField.setText("");
        mesField.setText("");
        anioField.setText("");
    }
}

package trabajopracticoswing.componentes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import trabajopracticoswing.util.VentanaUtil;

public class ComponentesBasicosDemo extends JFrame {

    private JTextField nombreField;
    private JCheckBox aceptarCheck;
    private JRadioButton alumnoRadio;
    private JRadioButton docenteRadio;

    public ComponentesBasicosDemo() {
        super("Componentes basicos");
        VentanaUtil.prepararVentana(this, 460, 280);
        crearContenido();
    }

    private void crearContenido() {
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        nombreField = new JTextField(20);
        aceptarCheck = new JCheckBox("Acepto practicar Swing");
        alumnoRadio = new JRadioButton("Alumno", true);
        docenteRadio = new JRadioButton("Docente");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(alumnoRadio);
        grupo.add(docenteRadio);

        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRadios.add(alumnoRadio);
        panelRadios.add(docenteRadio);

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(nombreField);
        panelFormulario.add(new JLabel("Rol:"));
        panelFormulario.add(panelRadios);
        panelFormulario.add(new JLabel("Confirmacion:"));
        panelFormulario.add(aceptarCheck);

        JButton boton = new JButton("Mostrar datos");
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDatos();
            }
        });

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(boton);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void mostrarDatos() {
        String nombre = nombreField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre.", "Dato requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rol = alumnoRadio.isSelected() ? "Alumno" : "Docente";
        String practica = aceptarCheck.isSelected() ? "Si" : "No";
        JOptionPane.showMessageDialog(this,
                "Nombre: " + nombre + "\nRol: " + rol + "\nAcepta practicar: " + practica,
                "Datos ingresados",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

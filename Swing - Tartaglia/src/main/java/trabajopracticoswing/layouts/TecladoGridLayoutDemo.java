package trabajopracticoswing.layouts;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import trabajopracticoswing.util.VentanaUtil;

public class TecladoGridLayoutDemo extends JFrame {

    private JTextField display;

    public TecladoGridLayoutDemo() {
        super("GridLayout - Teclado numerico");
        VentanaUtil.prepararVentana(this, 320, 420);
        crearContenido();
    }

    private void crearContenido() {
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelTeclado = new JPanel(new GridLayout(4, 3, 6, 6));
        panelTeclado.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        for (int i = 1; i <= 9; i++) {
            agregarBoton(panelTeclado, String.valueOf(i));
        }
        agregarBoton(panelTeclado, "*");
        agregarBoton(panelTeclado, "0");
        agregarBoton(panelTeclado, "#");

        add(display, BorderLayout.NORTH);
        add(panelTeclado, BorderLayout.CENTER);
    }

    private void agregarBoton(JPanel panel, final String texto) {
        JButton boton = new JButton(texto);
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setText(display.getText() + texto);
            }
        });
        panel.add(boton);
    }
}

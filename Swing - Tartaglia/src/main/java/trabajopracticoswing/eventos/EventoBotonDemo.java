package trabajopracticoswing.eventos;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import trabajopracticoswing.util.VentanaUtil;

public class EventoBotonDemo extends JFrame {

    private int cantidadClicks = 0;
    private JLabel contadorLabel;

    public EventoBotonDemo() {
        super("Manejo de eventos - JButton");
        VentanaUtil.prepararVentana(this, 360, 180);
        crearContenido();
    }

    private void crearContenido() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 24));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton boton = new JButton("Pulsame");
        contadorLabel = new JLabel("Clicks: 0");

        boton.addActionListener(new EventoBotonPulsado());

        panel.add(boton);
        panel.add(contadorLabel);
        add(panel);
    }

    private class EventoBotonPulsado implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cantidadClicks++;
            JButton boton = (JButton) e.getSource();
            boton.setText("Gracias");
            contadorLabel.setText("Clicks: " + cantidadClicks);
        }
    }
}

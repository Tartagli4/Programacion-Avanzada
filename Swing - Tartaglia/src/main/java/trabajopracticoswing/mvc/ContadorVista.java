package trabajopracticoswing.mvc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ContadorVista extends JPanel {

    private final JLabel valorLabel;
    private final JButton incrementarButton;
    private final JButton reiniciarButton;

    public ContadorVista() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        valorLabel = new JLabel("0", SwingConstants.CENTER);
        incrementarButton = new JButton("Incrementar");
        reiniciarButton = new JButton("Reiniciar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(incrementarButton);
        panelBotones.add(reiniciarButton);

        add(new JLabel("Ejemplo simple de MVC", SwingConstants.CENTER), BorderLayout.NORTH);
        add(valorLabel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void actualizar(int valor) {
        valorLabel.setText(String.valueOf(valor));
    }

    public JButton getIncrementarButton() {
        return incrementarButton;
    }

    public JButton getReiniciarButton() {
        return reiniciarButton;
    }
}

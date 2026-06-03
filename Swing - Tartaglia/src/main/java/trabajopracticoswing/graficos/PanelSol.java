package trabajopracticoswing.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PanelSol extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int radio = Math.min(getWidth(), getHeight()) / 6;
        int largoRayo = radio + 45;

        g.setColor(Color.ORANGE);
        g.fillOval(centroX - radio, centroY - radio, radio * 2, radio * 2);

        for (double angulo = 0; angulo < 2 * Math.PI; angulo += 0.25) {
            int inicioX = (int) (centroX + radio * Math.cos(angulo));
            int inicioY = (int) (centroY + radio * Math.sin(angulo));
            int finX = (int) (centroX + largoRayo * Math.cos(angulo));
            int finY = (int) (centroY + largoRayo * Math.sin(angulo));
            g.drawLine(inicioX, inicioY, finX, finY);
        }

        g.setColor(Color.BLACK);
        g.fillOval(centroX - 35, centroY - 25, 14, 14);
        g.fillOval(centroX + 22, centroY - 25, 14, 14);
        g.drawArc(centroX - 35, centroY - 25, 70, 65, 200, 140);

        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString("Dibujo personalizado con paintComponent", 20, 25);
    }
}

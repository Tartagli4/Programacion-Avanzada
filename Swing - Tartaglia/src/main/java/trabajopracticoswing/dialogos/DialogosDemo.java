package trabajopracticoswing.dialogos;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import trabajopracticoswing.util.VentanaUtil;

public class DialogosDemo extends JFrame {

    public DialogosDemo() {
        super("JOptionPane - Dialogos predefinidos");
        VentanaUtil.prepararVentana(this, 400, 260);
        crearContenido();
    }

    private void crearContenido() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JButton mensaje = new JButton("Mostrar mensaje");
        JButton entrada = new JButton("Pedir dato");
        JButton confirmar = new JButton("Confirmar accion");

        mensaje.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DialogosDemo.this, "Este es un mensaje informativo.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        entrada.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(DialogosDemo.this, "Ingrese su nombre:", "Alumno");
                if (nombre != null && !nombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(DialogosDemo.this, "Nombre ingresado: " + nombre.trim());
                }
            }
        });

        confirmar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(DialogosDemo.this, "Desea continuar?", "Confirmacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                String texto = respuesta == JOptionPane.YES_OPTION ? "Eligio continuar." : "Cancelo la accion.";
                JOptionPane.showMessageDialog(DialogosDemo.this, texto);
            }
        });

        panel.add(mensaje);
        panel.add(entrada);
        panel.add(confirmar);
        add(panel);
    }
}

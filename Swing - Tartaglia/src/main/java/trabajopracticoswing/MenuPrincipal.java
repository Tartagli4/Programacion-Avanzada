package trabajopracticoswing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import trabajopracticoswing.componentes.ComponentesBasicosDemo;
import trabajopracticoswing.dialogos.DialogosDemo;
import trabajopracticoswing.eventos.EventoBotonDemo;
import trabajopracticoswing.eventos.SaludoConEventoDemo;
import trabajopracticoswing.graficos.VentanaSolDemo;
import trabajopracticoswing.layouts.BorderLayoutDemo;
import trabajopracticoswing.layouts.FlowLayoutDemo;
import trabajopracticoswing.layouts.FormularioGridBagDemo;
import trabajopracticoswing.layouts.FormularioUsuarioPanelesDemo;
import trabajopracticoswing.layouts.TecladoGridLayoutDemo;
import trabajopracticoswing.mvc.ContadorMVCDemo;
import trabajopracticoswing.util.VentanaUtil;
import trabajopracticoswing.ventanas.VentanaBasicaDemo;
import trabajopracticoswing.ventanas.VentanaExtendidaDemo;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        super("Trabajo Practico Swing - Menu principal");
        configurarVentana();
        crearContenido();
    }

    private void configurarVentana() {
        VentanaUtil.prepararVentana(this, 520, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void crearContenido() {
        JLabel titulo = new JLabel("Ejemplos de Java Swing", SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel descripcion = new JLabel("Seleccione una clase para ejecutar el ejemplo mejorado", SwingConstants.CENTER);
        descripcion.setBorder(BorderFactory.createEmptyBorder(0, 16, 12, 16));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(descripcion, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 8, 8));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(12, 24, 24, 24));

        agregarBoton(panelBotones, "1. Ventana basica", new Runnable() {
            @Override public void run() { new VentanaBasicaDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "2. Ventana extendida", new Runnable() {
            @Override public void run() { new VentanaExtendidaDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "3. Componentes basicos", new Runnable() {
            @Override public void run() { new ComponentesBasicosDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "4. FlowLayout", new Runnable() {
            @Override public void run() { new FlowLayoutDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "5. GridLayout - teclado", new Runnable() {
            @Override public void run() { new TecladoGridLayoutDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "6. BorderLayout", new Runnable() {
            @Override public void run() { new BorderLayoutDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "7. JPanel - formulario", new Runnable() {
            @Override public void run() { new FormularioUsuarioPanelesDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "8. GridBagLayout - formulario avanzado", new Runnable() {
            @Override public void run() { new FormularioGridBagDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "9. Evento de boton", new Runnable() {
            @Override public void run() { new EventoBotonDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "10. Saludo con evento", new Runnable() {
            @Override public void run() { new SaludoConEventoDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "11. Dialogos predefinidos", new Runnable() {
            @Override public void run() { new DialogosDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "12. Dibujo de graficos", new Runnable() {
            @Override public void run() { new VentanaSolDemo().setVisible(true); }
        });
        agregarBoton(panelBotones, "13. Arquitectura MVC", new Runnable() {
            @Override public void run() { new ContadorMVCDemo().setVisible(true); }
        });

        add(encabezado, BorderLayout.NORTH);
        add(new JScrollPane(panelBotones), BorderLayout.CENTER);
    }

    private void agregarBoton(JPanel panel, String texto, final Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accion.run();
            }
        });
        panel.add(boton);
    }
}

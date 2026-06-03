package trabajopracticoswing.mvc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContadorControlador {

    private final ContadorModelo modelo;
    private final ContadorVista vista;

    public ContadorControlador(ContadorModelo modelo, ContadorVista vista) {
        this.modelo = modelo;
        this.vista = vista;
        conectarEventos();
    }

    private void conectarEventos() {
        vista.getIncrementarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelo.incrementar();
            }
        });

        vista.getReiniciarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelo.reiniciar();
            }
        });
    }
}

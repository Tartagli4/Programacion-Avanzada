# Trabajo Práctico Swing

Proyecto desarrollado en Java Swing para practicar los ejemplos del apunte `Swing_presentación_completa1.pdf`.

## Alumno

Completar con nombre y apellido.

## Descripción

El proyecto contiene clases individuales organizadas por tema. Cada clase toma como base uno de los ejemplos del apunte y le agrega una mejora simple: validaciones, mensajes de usuario, mejor distribución visual, uso de paneles, menú principal o separación MVC.

## Contenido del proyecto

- `ventanas`: creación de ventanas con `JFrame`.
- `componentes`: uso de `JLabel`, `JTextField`, `JButton`, `JCheckBox` y `JRadioButton`.
- `layouts`: ejemplos con `FlowLayout`, `GridLayout`, `BorderLayout`, `JPanel` y `GridBagLayout`.
- `eventos`: manejo de eventos con `ActionListener`.
- `dialogos`: cuadros predefinidos con `JOptionPane`.
- `graficos`: dibujo personalizado usando `paintComponent(Graphics g)`.
- `mvc`: ejemplo simple usando Modelo, Vista y Controlador.

## Cómo abrirlo en NetBeans

1. Abrir NetBeans.
2. Ir a `File > Open Project`.
3. Seleccionar la carpeta `TrabajoPracticoSwing`.
4. Abrir el proyecto Maven.
5. Ejecutar la clase principal:

```text
trabajopracticoswing.TrabajoPracticoSwingApplication
```

## Cómo ejecutarlo por consola

```bash
mvn clean compile exec:java
```

## Clases principales

- `TrabajoPracticoSwingApplication`: punto de entrada del programa.
- `MenuPrincipal`: menú para abrir todos los ejemplos.
- `VentanaBasicaDemo`: ejemplo básico de ventana.
- `VentanaExtendidaDemo`: ejemplo extendiendo `JFrame`.
- `ComponentesBasicosDemo`: componentes básicos de Swing.
- `FlowLayoutDemo`: distribución con `FlowLayout`.
- `TecladoGridLayoutDemo`: distribución con `GridLayout`.
- `BorderLayoutDemo`: distribución con `BorderLayout`.
- `FormularioUsuarioPanelesDemo`: formulario compuesto con `JPanel`.
- `FormularioGridBagDemo`: formulario con `GridBagLayout`.
- `EventoBotonDemo`: evento de pulsación de botón.
- `SaludoConEventoDemo`: acceso a un `JTextField` desde un evento.
- `DialogosDemo`: cuadros de diálogo con `JOptionPane`.
- `VentanaSolDemo` y `PanelSol`: dibujo personalizado.
- `ContadorMVCDemo`: ejemplo básico de arquitectura MVC.

## Observación

El alcance se mantuvo simple y académico: el objetivo principal es demostrar los conceptos del apunte sin convertir el trabajo en una aplicación demasiado grande.

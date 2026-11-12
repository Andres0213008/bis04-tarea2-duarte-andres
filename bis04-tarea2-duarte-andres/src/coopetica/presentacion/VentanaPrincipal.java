package coopetica.presentacion;

import coopetica.logica.AsociadoServicio;
import coopetica.modelo.Asociado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VentanaPrincipal extends JFrame {

    // TODO: cambie esto por su nombre completo (requisito 5)
    private static final String NOMBRE_ESTUDIANTE = "Andres Duarte Moscoso";

    private static final String ERROR_DATOS =
            "No se pudo leer o escribir el archivo de datos. "
            + "Revise que datos/asociados.txt no tenga lineas danadas.";

    private final AsociadoServicio servicio;

    private JTextField campoCedula;
    private JTextField campoNombre;
    private JTextField campoSaldo;
    private JTextField campoBusqueda;
    private JButton botonRegistrar;
    private JButton botonListar;
    private JButton botonResumen;
    private JButton botonBuscar;
    private JTextArea areaResultados;
    private JLabel estado;

    public VentanaPrincipal(AsociadoServicio servicio) {
        this.servicio = servicio;

        // 2. Configurar la ventana
        setTitle("CoopeTica - Asociados - " + NOMBRE_ESTUDIANTE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Componentes
        campoCedula = new JTextField(10);
        campoNombre = new JTextField(16);
        campoSaldo = new JTextField(9);
        campoBusqueda = new JTextField(10);
        botonRegistrar = new JButton("Registrar");
        botonListar = new JButton("Listar asociados");
        botonResumen = new JButton("Resumen");
        botonBuscar = new JButton("Buscar");
        areaResultados = new JTextArea(14, 50);
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        estado = new JLabel("Listo.");
        estado.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        // 3. Paneles
        JLabel titulo = new JLabel("CoopeTica R.L. - " + NOMBRE_ESTUDIANTE, JLabel.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 15f));
        titulo.setBorder(BorderFactory.createEmptyBorder(6, 0, 2, 0));

        JPanel formulario = new JPanel();
        formulario.setBorder(BorderFactory.createTitledBorder("Registrar asociado"));
        formulario.add(new JLabel("Cedula:"));
        formulario.add(campoCedula);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Saldo de aportes:"));
        formulario.add(campoSaldo);
        formulario.add(botonRegistrar);

        JPanel busqueda = new JPanel();
        busqueda.setBorder(BorderFactory.createTitledBorder("Buscar por cedula"));
        busqueda.add(new JLabel("Cedula:"));
        busqueda.add(campoBusqueda);
        busqueda.add(botonBuscar);

        JPanel acciones = new JPanel();
        acciones.setBorder(BorderFactory.createTitledBorder("Consultas"));
        acciones.add(botonListar);
        acciones.add(botonResumen);

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.add(titulo);
        norte.add(formulario);
        norte.add(busqueda);
        norte.add(acciones);

        // 4. Agregar a la ventana
        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(areaResultados), BorderLayout.CENTER);
        add(estado, BorderLayout.SOUTH);

        // 5. Conectar botones (un boton, un metodo)
        botonRegistrar.addActionListener(e -> registrar());
        botonListar.addActionListener(e -> listar());
        botonResumen.addActionListener(e -> mostrarResumen());
        botonBuscar.addActionListener(e -> buscar());

        // 6. Cerrar
        pack();
        setLocationRelativeTo(null);

        listar(); // muestra de entrada lo que ya hay guardado
    }

    private void registrar() {
        try {
            String resultado = servicio.registrar(
                    campoCedula.getText(),
                    campoNombre.getText(),
                    campoSaldo.getText());

            if (resultado.equals(AsociadoServicio.OK)) {
                campoCedula.setText("");
                campoNombre.setText("");
                campoSaldo.setText("");
                campoCedula.requestFocus();
                listar();
                mostrarEstado("Asociado registrado correctamente.", false);
                JOptionPane.showMessageDialog(this,
                        "Asociado registrado correctamente.",
                        "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarEstado(resultado, true);
                JOptionPane.showMessageDialog(this, resultado,
                        "No se pudo registrar", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException | NumberFormatException e) {
            mostrarErrorDatos();
        }
    }

    private void listar() {
        try {
            ArrayList<Asociado> lista = servicio.listar();
            if (lista.isEmpty()) {
                areaResultados.setText("No hay asociados registrados.");
            } else {
                StringBuilder texto = new StringBuilder();
                for (Asociado a : lista) {
                    texto.append(a).append("\n");
                }
                areaResultados.setText(texto.toString());
            }
            mostrarEstado("Asociados cargados: " + lista.size(), false);
        } catch (IOException | NumberFormatException e) {
            mostrarErrorDatos();
        }
    }

    private void mostrarResumen() {
        try {
            int cantidad = servicio.cantidadAsociados();
            double total = servicio.totalAportes();
            JOptionPane.showMessageDialog(this,
                    "Asociados registrados: " + cantidad + "\n"
                    + "Total de aportes: " + String.format("%,.2f", total),
                    "Resumen", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | NumberFormatException e) {
            mostrarErrorDatos();
        }
    }

    private void buscar() {
        try {
            Asociado a = servicio.buscarPorCedula(campoBusqueda.getText());
            if (a == null) {
                String mensaje = "No existe un asociado con la cedula "
                        + campoBusqueda.getText().trim() + ".";
                areaResultados.setText(mensaje);
                mostrarEstado(mensaje, true);
            } else {
                areaResultados.setText("Asociado encontrado\n"
                        + "Cedula: " + a.getCedula() + "\n"
                        + "Nombre: " + a.getNombre() + "\n"
                        + "Saldo de aportes: " + String.format("%,.2f", a.getSaldoAportes()));
                mostrarEstado("Busqueda completada.", false);
            }
        } catch (IOException | NumberFormatException e) {
            mostrarErrorDatos();
        }
    }

    private void mostrarEstado(String mensaje, boolean esError) {
        estado.setText(mensaje);
        estado.setForeground(esError ? Color.RED : new Color(0, 110, 0));
    }

    private void mostrarErrorDatos() {
        mostrarEstado(ERROR_DATOS, true);
        JOptionPane.showMessageDialog(this, ERROR_DATOS,
                "Error de datos", JOptionPane.ERROR_MESSAGE);
    }
}

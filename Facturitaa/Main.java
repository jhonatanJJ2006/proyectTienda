package Facturitaa;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    private static final float IVA = 0.16f;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Ventana ventana = new Ventana();
                ventana.setBounds(100, 100, 1000, 700);
                ventana.setVisible(true);
                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    static class Ventana extends JFrame {
        private JLabel etiqueta;
        private JTextField textoNombre, textoApellido, textoCedula, textoCelular;
        private JButton btnCalcular, btnAñadirProducto;
        private Producto[] productos;
        private JList<String> listaProductos;
        private DefaultListModel<String> model;
        private ArrayList<Producto> productosSeleccionados;

        public Ventana() {
            setTitle("Sistema de Facturación");
            setLayout(new BorderLayout());
            setBackground(Color.DARK_GRAY);

            // Crear productos
            productos = new Producto[] {
                    new Producto("DP001", "Dragon Pharma", 30.00f, 100),
                    new Producto("GS002", "Sebas Strong", 25.00f, 100),
                    new Producto("PP003", "Pepe's Muscle", 40.00f, 100),
                    new Producto("JS004", "Java's ++", 35.00f, 100),
                    new Producto("ML005", "Mamalong", 27.00f, 100)
            };

            // Inicializar la lista de productos seleccionados
            productosSeleccionados = new ArrayList<>();

            // Crear componentes
            JPanel panelForm = new JPanel(new GridLayout(6, 2));
            panelForm.setBackground(Color.DARK_GRAY);

            etiqueta = new JLabel("Ingrese los datos del receptor:");
            etiqueta.setForeground(Color.WHITE);

            textoNombre = new JTextField(9);
            textoNombre.setPreferredSize(new Dimension(300, 25));
            textoApellido = new JTextField(9);
            textoApellido.setPreferredSize(new Dimension(300, 25));
            textoCedula = new JTextField(9);
            textoCedula.setPreferredSize(new Dimension(300, 25));
            textoCelular = new JTextField(9);
            textoCelular.setPreferredSize(new Dimension(300, 25));

            // Cambiar los textos de los labels a blanco
            JLabel labelNombre = new JLabel("Nombre:");
            labelNombre.setForeground(Color.WHITE);
            JLabel labelApellido = new JLabel("Apellido:");
            labelApellido.setForeground(Color.WHITE);
            JLabel labelCedula = new JLabel("Cédula/RUC:");
            labelCedula.setForeground(Color.WHITE);
            JLabel labelCelular = new JLabel("Celular:");
            labelCelular.setForeground(Color.WHITE);

            panelForm.add(labelNombre);
            panelForm.add(textoNombre);
            panelForm.add(labelApellido);
            panelForm.add(textoApellido);
            panelForm.add(labelCedula);
            panelForm.add(textoCedula);
            panelForm.add(labelCelular);
            panelForm.add(textoCelular);

            // Crear botón para calcular total y añadir productos
            btnCalcular = new JButton("Calcular Total");
            btnAñadirProducto = new JButton("Añadir Producto");

            // Crear modelo de lista y JList para productos
            model = new DefaultListModel<>();
            for (Producto producto : productos) {
                model.addElement(producto.getCodigo() + " - " + producto.getNombre() + " - $" + producto.getPrecio()
                        + " | Existencias: "
                        + producto.getCantidad());
            }
            listaProductos = new JList<>(model);
            listaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Cambiar color del texto en el JList
            listaProductos.setForeground(Color.WHITE);
            listaProductos.setBackground(Color.DARK_GRAY);

            // Cambiar el color del encabezado de los productos a blanco
            JLabel labelProductos = new JLabel("Seleccione un producto:");
            labelProductos.setForeground(Color.WHITE);

            // Panel de lista de productos
            JPanel panelListaProductos = new JPanel();
            panelListaProductos.setBackground(Color.DARK_GRAY);
            panelListaProductos.setLayout(new BorderLayout());
            panelListaProductos.add(labelProductos, BorderLayout.NORTH);
            panelListaProductos.add(new JScrollPane(listaProductos), BorderLayout.CENTER);

            // Panel de botones
            JPanel panelBotones = new JPanel();
            panelBotones.setBackground(Color.DARK_GRAY);
            panelBotones.add(btnAñadirProducto);
            panelBotones.add(btnCalcular);

            // Agregar paneles a la ventana
            add(etiqueta, BorderLayout.NORTH);
            add(panelForm, BorderLayout.WEST);
            add(panelListaProductos, BorderLayout.CENTER);
            add(panelBotones, BorderLayout.PAGE_END);

            // Acción del botón de añadir producto
            btnAñadirProducto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    añadirProducto();
                }
            });

            // Acción del botón de calcular
            btnCalcular.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    realizarCompra();
                }
            });
        }

        // Método para añadir un producto a la compra
        private void añadirProducto() {
            // Obtener el producto seleccionado
            String productoSeleccionado = listaProductos.getSelectedValue();
            if (productoSeleccionado != null) {
                // Extraer el código del producto
                String codigoProducto = productoSeleccionado.split(" - ")[0];
                Producto producto = buscarProducto(codigoProducto);
                if (producto != null) {
                    // Solicitar la cantidad
                    int cantidad = Integer
                            .parseInt(JOptionPane.showInputDialog(this, "¿Cuántas unidades desea comprar?"));
                    if (producto.vender(cantidad)) {
                        producto.setCantidad(cantidad);
                        productosSeleccionados.add(producto);
                        JOptionPane.showMessageDialog(this, "Producto añadido a la compra.");
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No hay suficientes existencias para el producto seleccionado.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto.");
            }
        }

        // Método para realizar la compra
        private void realizarCompra() {
            if (textoNombre.getText().isEmpty() || textoApellido.getText().isEmpty() || textoCedula.getText().isEmpty()
                    || textoCelular.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, complete todos los campos del receptor antes de continuar.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            ingresarDatosReceptor();

            StringBuilder detallesCompra = new StringBuilder();
            float totalCompra = 0;

            // Mostrar inventario
            detallesCompra.append("\n\t| --- Productos Seleccionados --- |\n");
            for (Producto producto : productosSeleccionados) {
                detallesCompra.append(producto.getNombre()).append(" - Cantidad: ").append(producto.getCantidad())
                        .append("\n");
                totalCompra += producto.total(producto.getCantidad());
            }

            mostrarFactura(detallesCompra.toString(), totalCompra);
        }

        // Buscar producto por código
        private Producto buscarProducto(String codigo) {
            for (Producto producto : productos) {
                if (producto.getCodigo().equals(codigo)) {
                    return producto;
                }
            }
            return null;
        }

        // Mostrar Factura
        private void mostrarFactura(String detallesCompra, float subtotal) {
            DecimalFormat df = new DecimalFormat();
            float valorIva = subtotal * IVA;
            float totalConIVA = subtotal + valorIva;

            // Fecha de emisión
            String fecha = LocalDate.now().toString();

            // Detalles de la factura
            StringBuilder factura = new StringBuilder();
            factura.append("==========================================================\n");
            factura.append("                             FACTURA                      \n");
            factura.append("                          --- Tienda ---          \n");
            factura.append("==========================================================\n");
            factura.append(String.format("R.U.C.: %-35s\n", "1790112233001"));
            factura.append(String.format("FACTURA NO.: %-30s\n", "002-001-123456789"));
            factura.append(String.format("AUT. SRI: %-31s\n", "1234567890"));
            factura.append("----------------------------------------------------------\n");
            factura.append(String.format("Sr(es): %-30s\n", textoNombre.getText() + " " + textoApellido.getText()));
            factura.append(String.format("R.U.C./C.I.: %-30s\n", textoCedula.getText()));
            factura.append(String.format("Fecha Emisión: %-25s\n", fecha));
            factura.append("----------------------------------------------------------\n");
            factura.append(String.format("%-5s %-20s %-12s %-12s\n", "Cant", "Producto", "P. Unitario", "V. Total"));
            factura.append("----------------------------------------------------------\n");

            // Detallar los productos
            for (Producto producto : productosSeleccionados) {
                factura.append(String.format("%-8d %-21s %-18s %-14s\n",
                        producto.getCantidad(),
                        producto.getNombre(),
                        df.format(producto.getPrecio()),
                        df.format(producto.total(producto.getCantidad()))));
            }

            factura.append("----------------------------------------------------------\n");
            factura.append(String.format("%-55s %-12s\n", "Subtotal: ", df.format(subtotal)));
            factura.append(String.format("%-54s %-12s\n", "IVA (16%): ", df.format(valorIva)));
            factura.append(String.format("%-51s %-12s\n", "Total a pagar: ", df.format(totalConIVA)));
            factura.append("==========================================================\n");

            JOptionPane.showMessageDialog(this, factura.toString(), "Factura", JOptionPane.INFORMATION_MESSAGE);
        }

        // Ingresar datos del receptor
        private void ingresarDatosReceptor() {
            String nombre = textoNombre.getText();
            String apellido = textoApellido.getText();
            String cedula = textoCedula.getText();
            String celular = textoCelular.getText();
            LocalDate diaCompra = LocalDate.now();
        }
    }
}

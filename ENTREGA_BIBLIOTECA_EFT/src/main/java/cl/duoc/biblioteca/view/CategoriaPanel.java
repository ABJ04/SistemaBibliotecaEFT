package cl.duoc.biblioteca.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public final class CategoriaPanel extends JPanel {
    private final JTextField nombre = new JTextField(18);
    private final JTextField descripcion = new JTextField(30);
    private final JButton crear = new JButton("Crear");
    private final JButton actualizar = new JButton("Actualizar");
    private final JButton eliminar = new JButton("Eliminar");
    private final JButton limpiar = new JButton("Limpiar");
    private final DefaultTableModel model = LibroPanel.readonlyModel("ID","Nombre","Descripción");
    private final JTable table = new JTable(model);

    public CategoriaPanel() {
        super(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Nombre")); top.add(nombre);
        top.add(new JLabel("Descripción")); top.add(descripcion);
        top.add(crear); top.add(actualizar); top.add(eliminar); top.add(limpiar);
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    public JTextField getNombre() { return nombre; }
    public JTextField getDescripcion() { return descripcion; }
    public JButton getCrear() { return crear; }
    public JButton getActualizar() { return actualizar; }
    public JButton getEliminar() { return eliminar; }
    public JButton getLimpiar() { return limpiar; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
}

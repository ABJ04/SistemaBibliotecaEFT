package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public final class LibroPanel extends JPanel {
    private final JTextField titulo = new JTextField(14);
    private final JTextField autor = new JTextField(12);
    private final JTextField editorial = new JTextField(12);
    private final JTextField isbn = new JTextField(11);
    private final JTextField stock = new JTextField(5);
    private final JComboBox<Categoria> categoria = new JComboBox<>();
    private final JButton crear = new JButton("Crear");
    private final JButton actualizar = new JButton("Actualizar");
    private final JButton eliminar = new JButton("Eliminar");
    private final JButton limpiar = new JButton("Limpiar");
    private final JButton recargar = new JButton("Recargar");
    private final DefaultTableModel model = readonlyModel("ID","Título","Autor","Editorial","ISBN","Stock","Categoría ID","Categoría");
    private final JTable table = new JTable(model);
    private final JPanel editor = new JPanel(new FlowLayout(FlowLayout.LEFT));

    public LibroPanel(boolean editable) {
        super(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        editor.add(new JLabel("Título")); editor.add(titulo);
        editor.add(new JLabel("Autor")); editor.add(autor);
        editor.add(new JLabel("Editorial")); editor.add(editorial);
        editor.add(new JLabel("ISBN")); editor.add(isbn);
        editor.add(new JLabel("Stock")); editor.add(stock);
        editor.add(new JLabel("Categoría")); editor.add(categoria);
        editor.add(crear); editor.add(actualizar); editor.add(eliminar); editor.add(limpiar);
        if (editable) add(editor, BorderLayout.NORTH);
        else add(new JLabel("Consulta de catálogo (solo lectura)"), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT)); south.add(recargar); add(south, BorderLayout.SOUTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static DefaultTableModel readonlyModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }
    public JTextField getTitulo() { return titulo; }
    public JTextField getAutor() { return autor; }
    public JTextField getEditorial() { return editorial; }
    public JTextField getIsbn() { return isbn; }
    public JTextField getStock() { return stock; }
    public JComboBox<Categoria> getCategoria() { return categoria; }
    public JButton getCrear() { return crear; }
    public JButton getActualizar() { return actualizar; }
    public JButton getEliminar() { return eliminar; }
    public JButton getLimpiar() { return limpiar; }
    public JButton getRecargar() { return recargar; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
}

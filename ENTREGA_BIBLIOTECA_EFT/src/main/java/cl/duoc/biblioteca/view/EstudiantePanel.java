package cl.duoc.biblioteca.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public final class EstudiantePanel extends JPanel {
    private final JTextField rut = new JTextField(9);
    private final JTextField nombres = new JTextField(10);
    private final JTextField apellidos = new JTextField(10);
    private final JTextField email = new JTextField(14);
    private final JTextField curso = new JTextField(7);
    private final JTextField username = new JTextField(9);
    private final JPasswordField password = new JPasswordField(9);
    private final JButton crear = new JButton("Crear");
    private final JButton actualizar = new JButton("Actualizar");
    private final JButton eliminar = new JButton("Eliminar");
    private final JButton limpiar = new JButton("Limpiar");
    private final DefaultTableModel model = LibroPanel.readonlyModel("ID","RUT","Nombres","Apellidos","Email","Curso","Usuario");
    private final JTable table = new JTable(model);

    public EstudiantePanel() {
        super(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("RUT")); top.add(rut); top.add(new JLabel("Nombres")); top.add(nombres);
        top.add(new JLabel("Apellidos")); top.add(apellidos); top.add(new JLabel("Email")); top.add(email);
        top.add(new JLabel("Curso")); top.add(curso); top.add(new JLabel("Usuario")); top.add(username);
        top.add(new JLabel("Clave")); top.add(password); top.add(crear); top.add(actualizar); top.add(eliminar); top.add(limpiar);
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    public JTextField getRut() { return rut; }
    public JTextField getNombres() { return nombres; }
    public JTextField getApellidos() { return apellidos; }
    public JTextField getEmail() { return email; }
    public JTextField getCurso() { return curso; }
    public JTextField getUsername() { return username; }
    public JPasswordField getPassword() { return password; }
    public JButton getCrear() { return crear; }
    public JButton getActualizar() { return actualizar; }
    public JButton getEliminar() { return eliminar; }
    public JButton getLimpiar() { return limpiar; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
}

package cl.duoc.biblioteca.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public final class PrestamoPanel extends JPanel {
    private final JTextField estudianteId = new JTextField(6);
    private final JTextField libroId = new JTextField(6);
    private final JButton prestar = new JButton("Registrar préstamo");
    private final JButton devolver = new JButton("Devolver seleccionado");
    private final JButton recargar = new JButton("Recargar");
    private final JLabel status = new JLabel(" ");
    private final DefaultTableModel model = LibroPanel.readonlyModel(
            "Préstamo ID","Estudiante ID","Estudiante","Libro ID","Libro","Fecha préstamo","Vencimiento","Estado");
    private final JTable table = new JTable(model);

    public PrestamoPanel(Integer fixedStudentId) {
        super(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("ID estudiante")); top.add(estudianteId);
        top.add(new JLabel("ID libro")); top.add(libroId); top.add(prestar); top.add(devolver); top.add(recargar);
        if (fixedStudentId != null) {
            estudianteId.setText(String.valueOf(fixedStudentId));
            estudianteId.setEditable(false);
        }
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER);
        status.setBorder(BorderFactory.createEmptyBorder(4,4,4,4)); add(status, BorderLayout.SOUTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    public JTextField getEstudianteId() { return estudianteId; }
    public JTextField getLibroId() { return libroId; }
    public JButton getPrestar() { return prestar; }
    public JButton getDevolver() { return devolver; }
    public JButton getRecargar() { return recargar; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public void setStatus(String text) { status.setText(text); }
    public void setBusy(boolean busy) {
        prestar.setEnabled(!busy); devolver.setEnabled(!busy); recargar.setEnabled(!busy);
        if (busy) setStatus("Procesando en hilo independiente...");
    }
}

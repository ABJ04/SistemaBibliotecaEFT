package cl.duoc.biblioteca.view;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public final class ReportePanel extends JPanel {
    private final JButton masPrestados = new JButton("Libros más prestados");
    private final JButton actuales = new JButton("Actualmente prestados");
    private final JTextField estudianteId = new JTextField(6);
    private final JButton historial = new JButton("Historial por estudiante");
    private final JTable table = new JTable();

    public ReportePanel() {
        super(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(masPrestados); top.add(actuales); top.add(new JLabel("ID estudiante"));
        top.add(estudianteId); top.add(historial);
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER);
    }
    public JButton getMasPrestados() { return masPrestados; }
    public JButton getActuales() { return actuales; }
    public JTextField getEstudianteId() { return estudianteId; }
    public JButton getHistorial() { return historial; }
    public JTable getTable() { return table; }
}

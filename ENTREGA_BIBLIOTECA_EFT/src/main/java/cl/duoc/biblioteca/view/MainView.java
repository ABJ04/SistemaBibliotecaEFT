package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.model.Usuario;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public final class MainView extends JFrame {
    private final JTabbedPane tabs = new JTabbedPane();
    private final JButton logoutButton = new JButton("Cerrar sesión");

    public MainView(Usuario usuario) {
        super("Biblioteca Escolar");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel header = new JLabel("  Sesión: " + usuario.getNombreCompleto() + " | Rol: " + usuario.getRol());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        headerPanel.add(header, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        setMinimumSize(new Dimension(980, 620));
        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

    public void addTab(String title, JComponent component) { tabs.addTab(title, component); }
    public JButton getLogoutButton() { return logoutButton; }
}

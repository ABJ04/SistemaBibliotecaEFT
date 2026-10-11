package cl.duoc.biblioteca.view;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public final class LoginView extends JFrame {
    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final JButton loginButton = new JButton("Ingresar");
    private final JLabel statusLabel = new JLabel(" ");

    public LoginView() {
        super("Biblioteca Escolar - Ingreso");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        JLabel title = new JLabel("Sistema de Biblioteca Escolar", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        form.add(title, c);
        c.gridwidth = 1; c.gridy++;
        form.add(new JLabel("Usuario:"), c);
        c.gridx = 1; form.add(usernameField, c);
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; form.add(passwordField, c);
        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        form.add(loginButton, c);
        c.gridy++;
        statusLabel.setForeground(new Color(160, 30, 30));
        form.add(statusLabel, c);
        add(form);
        getRootPane().setDefaultButton(loginButton);
        pack();
        setLocationRelativeTo(null);
    }

    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public JButton getLoginButton() { return loginButton; }
    public void setStatus(String text) { statusLabel.setText(text); }
    public void setBusy(boolean busy) {
        loginButton.setEnabled(!busy);
        usernameField.setEnabled(!busy);
        passwordField.setEnabled(!busy);
        if (busy) setStatus("Validando credenciales...");
    }
}

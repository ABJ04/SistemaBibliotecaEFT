package cl.duoc.biblioteca;

import cl.duoc.biblioteca.controller.LoginController;
import cl.duoc.biblioteca.view.LoginView;

import javax.swing.*;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Se conserva el aspecto multiplataforma si el del sistema no está disponible.
            }
            LoginView view = new LoginView();
            new LoginController(view);
            view.setVisible(true);
        });
    }
}

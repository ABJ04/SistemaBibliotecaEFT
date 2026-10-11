package cl.duoc.biblioteca.view;

import javax.swing.*;
import java.awt.*;

public final class ViewMessages {
    private ViewMessages() {}
    public static void error(Component parent, Throwable error) {
        String message = error.getMessage();
        if (message == null && error.getCause() != null) message = error.getCause().getMessage();
        JOptionPane.showMessageDialog(parent, message == null ? error.toString() : message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Biblioteca", JOptionPane.INFORMATION_MESSAGE);
    }
    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}

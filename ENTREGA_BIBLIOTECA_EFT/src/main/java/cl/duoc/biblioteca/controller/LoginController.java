package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.dao.UsuarioDAO;
import cl.duoc.biblioteca.model.Usuario;
import cl.duoc.biblioteca.service.AuthenticationService;
import cl.duoc.biblioteca.view.LoginView;
import cl.duoc.biblioteca.view.ViewMessages;

import javax.swing.*;
import java.util.Optional;

public final class LoginController {
    private final LoginView view;
    private final AuthenticationService service = new AuthenticationService(new UsuarioDAO());

    public LoginController(LoginView view) {
        this.view = view;
        view.getLoginButton().addActionListener(event -> login());
    }

    private void login() {
        view.setBusy(true);
        SwingWorker<Optional<Usuario>, Void> worker = new SwingWorker<>() {
            @Override protected Optional<Usuario> doInBackground() throws Exception {
                return service.autenticar(view.getUsername(), view.getPassword());
            }
            @Override protected void done() {
                view.setBusy(false);
                try {
                    Optional<Usuario> user = get();
                    if (user.isEmpty()) {
                        view.setStatus("Usuario o contraseña incorrectos.");
                        return;
                    }
                    view.dispose();
                    new MainController(user.get()).show();
                } catch (Exception e) {
                    view.setStatus("No fue posible conectar con MySQL.");
                    ViewMessages.error(view, e);
                }
            }
        };
        worker.execute();
    }
}

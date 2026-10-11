package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.dao.UsuarioDAO;
import cl.duoc.biblioteca.model.Usuario;

import java.sql.SQLException;
import java.util.Optional;

public final class AuthenticationService {
    private final UsuarioDAO usuarioDAO;

    public AuthenticationService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Optional<Usuario> autenticar(String username, String password) throws SQLException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }
        return usuarioDAO.autenticar(username.trim(), password);
    }
}

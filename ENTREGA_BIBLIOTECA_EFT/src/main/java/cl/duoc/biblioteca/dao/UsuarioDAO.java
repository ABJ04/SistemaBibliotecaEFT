package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.model.Bibliotecario;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.model.Usuario;
import cl.duoc.biblioteca.util.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class UsuarioDAO {
    private static final String LOGIN_SQL = """
            SELECT u.id, u.username, u.rol, u.estudiante_id,
                   COALESCE(CONCAT(e.nombres, ' ', e.apellidos), 'Bibliotecario') nombre_completo,
                   e.rut, e.nombres, e.apellidos, e.email, e.curso, COALESCE(e.activo, TRUE) estudiante_activo
            FROM usuarios u
            LEFT JOIN estudiantes e ON e.id = u.estudiante_id
            WHERE u.username = ? AND u.password_hash = ? AND u.activo = TRUE
            """;

    public Optional<Usuario> autenticar(String username, String password) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(LOGIN_SQL)) {
            statement.setString(1, username);
            statement.setString(2, PasswordUtils.sha256(password));
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                if ("BIBLIOTECARIO".equals(rs.getString("rol"))) {
                    return Optional.of(new Bibliotecario(
                            rs.getInt("id"), rs.getString("username"), rs.getString("nombre_completo")));
                }
                if (!rs.getBoolean("estudiante_activo")) return Optional.empty();
                return Optional.of(new Estudiante(
                        rs.getInt("id"), rs.getString("username"), rs.getInt("estudiante_id"),
                        rs.getString("rut"), rs.getString("nombres"), rs.getString("apellidos"),
                        rs.getString("email"), rs.getString("curso"), true));
            }
        }
    }
}

package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.interfaces.CrudDAO;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.util.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class EstudianteDAO implements CrudDAO<Estudiante> {
    private static final String SELECT_BASE = """
            SELECT e.id estudiante_id, e.rut, e.nombres, e.apellidos, e.email, e.curso, e.activo,
                   COALESCE(u.id, 0) usuario_id, COALESCE(u.username, '') username
            FROM estudiantes e LEFT JOIN usuarios u ON u.estudiante_id = e.id
            """;

    @Override
    public Estudiante crear(Estudiante estudiante) throws SQLException {
        String studentSql = "INSERT INTO estudiantes(rut,nombres,apellidos,email,curso,activo) VALUES(?,?,?,?,?,TRUE)";
        String userSql = "INSERT INTO usuarios(username,password_hash,rol,estudiante_id,activo) VALUES(?,?,'ESTUDIANTE',?,TRUE)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(studentSql, Statement.RETURN_GENERATED_KEYS)) {
                    fillStudent(statement, estudiante);
                    statement.executeUpdate();
                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("No se generó el ID del estudiante.");
                        estudiante.setEstudianteId(keys.getInt(1));
                    }
                }
                try (PreparedStatement statement = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, estudiante.getUsername());
                    statement.setString(2, PasswordUtils.sha256(estudiante.getPasswordTemporal()));
                    statement.setInt(3, estudiante.getEstudianteIdValor());
                    statement.executeUpdate();
                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (keys.next()) estudiante.setId(keys.getInt(1));
                    }
                }
                connection.commit();
                return estudiante;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public List<Estudiante> listar() throws SQLException {
        List<Estudiante> result = new ArrayList<>();
        String sql = SELECT_BASE + " WHERE e.activo = TRUE ORDER BY e.apellidos, e.nombres";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) result.add(map(rs));
        }
        return result;
    }

    @Override
    public Optional<Estudiante> buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE e.id = ? AND e.activo = TRUE";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public boolean actualizar(Estudiante estudiante) throws SQLException {
        String studentSql = "UPDATE estudiantes SET rut=?,nombres=?,apellidos=?,email=?,curso=? WHERE id=? AND activo=TRUE";
        String userSql = "UPDATE usuarios SET username=? WHERE estudiante_id=?";
        String passwordSql = "UPDATE usuarios SET password_hash=? WHERE estudiante_id=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try {
                int changed;
                try (PreparedStatement statement = connection.prepareStatement(studentSql)) {
                    fillStudent(statement, estudiante);
                    statement.setInt(6, estudiante.getEstudianteIdValor());
                    changed = statement.executeUpdate();
                }
                try (PreparedStatement statement = connection.prepareStatement(userSql)) {
                    statement.setString(1, estudiante.getUsername());
                    statement.setInt(2, estudiante.getEstudianteIdValor());
                    statement.executeUpdate();
                }
                if (estudiante.getPasswordTemporal() != null && !estudiante.getPasswordTemporal().isBlank()) {
                    try (PreparedStatement statement = connection.prepareStatement(passwordSql)) {
                        statement.setString(1, PasswordUtils.sha256(estudiante.getPasswordTemporal()));
                        statement.setInt(2, estudiante.getEstudianteIdValor());
                        statement.executeUpdate();
                    }
                }
                connection.commit();
                return changed == 1;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String studentSql = "UPDATE estudiantes SET activo=FALSE WHERE id=?";
        String userSql = "UPDATE usuarios SET activo=FALSE WHERE estudiante_id=?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement student = connection.prepareStatement(studentSql);
                 PreparedStatement user = connection.prepareStatement(userSql)) {
                student.setInt(1, id);
                user.setInt(1, id);
                int changed = student.executeUpdate();
                user.executeUpdate();
                connection.commit();
                return changed == 1;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void fillStudent(PreparedStatement statement, Estudiante e) throws SQLException {
        statement.setString(1, e.getRut());
        statement.setString(2, e.getNombres());
        statement.setString(3, e.getApellidos());
        statement.setString(4, e.getEmail());
        statement.setString(5, e.getCurso());
    }

    private Estudiante map(ResultSet rs) throws SQLException {
        return new Estudiante(rs.getInt("usuario_id"), rs.getString("username"),
                rs.getInt("estudiante_id"), rs.getString("rut"), rs.getString("nombres"),
                rs.getString("apellidos"), rs.getString("email"), rs.getString("curso"),
                rs.getBoolean("activo"));
    }
}

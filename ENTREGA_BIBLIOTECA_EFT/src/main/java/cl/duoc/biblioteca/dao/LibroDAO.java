package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.interfaces.CrudDAO;
import cl.duoc.biblioteca.model.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class LibroDAO implements CrudDAO<Libro> {
    private static final String SELECT_BASE = """
            SELECT l.id, l.titulo, l.autor, l.editorial, l.isbn, l.stock,
                   l.categoria_id, c.nombre categoria_nombre
            FROM libros l JOIN categorias c ON c.id = l.categoria_id
            """;

    @Override
    public Libro crear(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros(titulo, autor, editorial, isbn, stock, categoria_id, activo) VALUES (?, ?, ?, ?, ?, ?, TRUE)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fill(statement, libro);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) libro.setId(keys.getInt(1));
            }
            return libro;
        }
    }

    @Override
    public List<Libro> listar() throws SQLException {
        List<Libro> result = new ArrayList<>();
        String sql = SELECT_BASE + " WHERE l.activo = TRUE ORDER BY l.titulo";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) result.add(map(rs));
        }
        return result;
    }

    @Override
    public Optional<Libro> buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE l.id = ? AND l.activo = TRUE";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public boolean actualizar(Libro libro) throws SQLException {
        String sql = "UPDATE libros SET titulo=?, autor=?, editorial=?, isbn=?, stock=?, categoria_id=? WHERE id=? AND activo=TRUE";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fill(statement, libro);
            statement.setInt(7, libro.getId());
            return statement.executeUpdate() == 1;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = """
                UPDATE libros l SET l.activo = FALSE
                WHERE l.id = ? AND NOT EXISTS (
                    SELECT 1 FROM prestamos p WHERE p.libro_id = l.id AND p.fecha_devolucion IS NULL
                )
                """;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    public int obtenerStockParaActualizar(Connection connection, int libroId) throws SQLException {
        String sql = "SELECT stock FROM libros WHERE id = ? AND activo = TRUE FOR UPDATE";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, libroId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) throw new SQLException("Libro inexistente o inactivo.");
                return rs.getInt("stock");
            }
        }
    }

    /** Actualiza el stock dentro de la transacción administrada por PrestamoService. */
    public void actualizarStock(Connection connection, int libroId, int nuevoStock) throws SQLException {
        if (nuevoStock < 0) throw new SQLException("El stock no puede quedar negativo.");
        String sql = "UPDATE libros SET stock = ? WHERE id = ? AND activo = TRUE";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nuevoStock);
            statement.setInt(2, libroId);
            if (statement.executeUpdate() != 1) throw new SQLException("No fue posible actualizar el stock.");
        }
    }

    private void fill(PreparedStatement statement, Libro libro) throws SQLException {
        statement.setString(1, libro.getTitulo());
        statement.setString(2, libro.getAutor());
        statement.setString(3, libro.getEditorial());
        statement.setString(4, libro.getIsbn());
        statement.setInt(5, libro.getStock());
        statement.setInt(6, libro.getCategoriaId());
    }

    private Libro map(ResultSet rs) throws SQLException {
        return new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"),
                rs.getString("editorial"), rs.getString("isbn"), rs.getInt("stock"),
                rs.getInt("categoria_id"), rs.getString("categoria_nombre"));
    }
}

package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.interfaces.CrudDAO;
import cl.duoc.biblioteca.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CategoriaDAO implements CrudDAO<Categoria> {
    @Override
    public Categoria crear(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categorias(nombre, descripcion, activo) VALUES (?, ?, TRUE)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, categoria.getNombre());
            statement.setString(2, categoria.getDescripcion());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) categoria.setId(keys.getInt(1));
            }
            return categoria;
        }
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> result = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM categorias WHERE activo = TRUE ORDER BY nombre";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) result.add(map(rs));
        }
        return result;
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, descripcion FROM categorias WHERE id = ? AND activo = TRUE";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public boolean actualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id = ? AND activo = TRUE";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoria.getNombre());
            statement.setString(2, categoria.getDescripcion());
            statement.setInt(3, categoria.getId());
            return statement.executeUpdate() == 1;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE categorias SET activo = FALSE WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    private Categoria map(ResultSet rs) throws SQLException {
        return new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"));
    }
}

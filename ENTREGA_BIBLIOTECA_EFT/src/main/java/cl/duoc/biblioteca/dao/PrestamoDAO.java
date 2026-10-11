package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.model.EstadoPrestamo;
import cl.duoc.biblioteca.model.Prestamo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PrestamoDAO {
    public Prestamo insertar(Connection connection, Prestamo prestamo) throws SQLException {
        String sql = """
                INSERT INTO prestamos(estudiante_id,libro_id,fecha_prestamo,fecha_vencimiento,estado)
                VALUES(?,?,?,?,'PRESTADO')
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, prestamo.getEstudianteId());
            statement.setInt(2, prestamo.getLibroId());
            statement.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            statement.setDate(4, Date.valueOf(prestamo.getFechaVencimiento()));
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("No se generó el préstamo.");
                prestamo.setId(keys.getInt(1));
            }
            return prestamo;
        }
    }

    public Optional<Prestamo> buscarActivoParaActualizar(Connection connection, int prestamoId) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE id=? AND fecha_devolucion IS NULL FOR UPDATE";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, prestamoId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public void marcarDevuelto(Connection connection, int prestamoId, LocalDate fecha, EstadoPrestamo estado) throws SQLException {
        String sql = "UPDATE prestamos SET fecha_devolucion=?, estado=? WHERE id=? AND fecha_devolucion IS NULL";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(fecha));
            statement.setString(2, estado.name());
            statement.setInt(3, prestamoId);
            if (statement.executeUpdate() != 1) throw new SQLException("El préstamo ya fue devuelto o no existe.");
        }
    }

    public void registrarHistorial(Connection connection, int prestamoId, String accion, String detalle) throws SQLException {
        String sql = "INSERT INTO historial_prestamos(prestamo_id,accion,detalle) VALUES(?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, prestamoId);
            statement.setString(2, accion);
            statement.setString(3, detalle);
            statement.executeUpdate();
        }
    }

    public List<Prestamo> listarActivos(Integer estudianteId) throws SQLException {
        StringBuilder sql = new StringBuilder("""
                SELECT p.*, CONCAT(e.nombres,' ',e.apellidos) estudiante_nombre, l.titulo libro_titulo
                FROM prestamos p
                JOIN estudiantes e ON e.id=p.estudiante_id
                JOIN libros l ON l.id=p.libro_id
                WHERE p.fecha_devolucion IS NULL
                """);
        if (estudianteId != null) sql.append(" AND p.estudiante_id=?");
        sql.append(" ORDER BY p.fecha_vencimiento");
        List<Prestamo> result = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            if (estudianteId != null) statement.setInt(1, estudianteId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) result.add(mapJoined(rs));
            }
        }
        return result;
    }

    private Prestamo map(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo();
        p.setId(rs.getInt("id"));
        p.setEstudianteId(rs.getInt("estudiante_id"));
        p.setLibroId(rs.getInt("libro_id"));
        p.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
        p.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
        Date returned = rs.getDate("fecha_devolucion");
        if (returned != null) p.setFechaDevolucion(returned.toLocalDate());
        p.setEstado(EstadoPrestamo.valueOf(rs.getString("estado")));
        return p;
    }

    private Prestamo mapJoined(ResultSet rs) throws SQLException {
        Prestamo p = map(rs);
        p.setEstudianteNombre(rs.getString("estudiante_nombre"));
        p.setLibroTitulo(rs.getString("libro_titulo"));
        return p;
    }
}

package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ReporteDAO {
    public List<Object[]> librosMasPrestados() throws SQLException {
        String sql = """
                SELECT l.id, l.titulo, l.autor, COUNT(p.id) total_prestamos
                FROM libros l LEFT JOIN prestamos p ON p.libro_id=l.id
                GROUP BY l.id,l.titulo,l.autor ORDER BY total_prestamos DESC,l.titulo
                """;
        return query(sql);
    }

    public List<Object[]> historialPorEstudiante(int estudianteId) throws SQLException {
        String sql = """
                SELECT p.id,l.titulo,p.fecha_prestamo,p.fecha_vencimiento,p.fecha_devolucion,p.estado
                FROM prestamos p JOIN libros l ON l.id=p.libro_id
                WHERE p.estudiante_id=? ORDER BY p.fecha_prestamo DESC
                """;
        return query(sql, estudianteId);
    }

    public List<Object[]> librosActualmentePrestados() throws SQLException {
        String sql = """
                SELECT p.id,l.titulo,CONCAT(e.nombres,' ',e.apellidos),p.fecha_prestamo,
                       p.fecha_vencimiento,
                       CASE WHEN CURRENT_DATE > p.fecha_vencimiento THEN 'ATRASADO' ELSE 'VIGENTE' END
                FROM prestamos p JOIN libros l ON l.id=p.libro_id
                JOIN estudiantes e ON e.id=p.estudiante_id
                WHERE p.fecha_devolucion IS NULL ORDER BY p.fecha_vencimiento
                """;
        return query(sql);
    }

    private List<Object[]> query(String sql, Object... parameters) throws SQLException {
        List<Object[]> result = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) statement.setObject(i + 1, parameters[i]);
            try (ResultSet rs = statement.executeQuery()) {
                int columns = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Object[] row = new Object[columns];
                    for (int i = 0; i < columns; i++) row[i] = rs.getObject(i + 1);
                    result.add(row);
                }
            }
        }
        return result;
    }
}

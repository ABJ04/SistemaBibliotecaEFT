package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.dao.PrestamoDAO;
import cl.duoc.biblioteca.model.EstadoPrestamo;
import cl.duoc.biblioteca.model.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class PrestamoService {
    public static final int DIAS_PRESTAMO = 14;

    private final PrestamoDAO prestamoDAO;
    private final LibroDAO libroDAO;

    public PrestamoService(PrestamoDAO prestamoDAO, LibroDAO libroDAO) {
        this.prestamoDAO = prestamoDAO;
        this.libroDAO = libroDAO;
    }

    /**
     * synchronized evita que dos hilos de esta aplicación descuenten el mismo stock a la vez.
     * SELECT ... FOR UPDATE agrega protección transaccional frente a otras instancias.
     */
    public synchronized Prestamo registrarPrestamo(int estudianteId, int libroId) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try {
                validarEstudianteActivo(connection, estudianteId);
                validarSinPrestamoDuplicado(connection, estudianteId, libroId);
                int stock = libroDAO.obtenerStockParaActualizar(connection, libroId);
                if (stock <= 0) throw new SQLException("No existe stock disponible para el libro seleccionado.");

                LocalDate hoy = LocalDate.now();
                Prestamo prestamo = new Prestamo();
                prestamo.setEstudianteId(estudianteId);
                prestamo.setLibroId(libroId);
                prestamo.setFechaPrestamo(hoy);
                prestamo.setFechaVencimiento(calcularVencimiento(hoy));
                prestamo.setEstado(EstadoPrestamo.PRESTADO);

                prestamoDAO.insertar(connection, prestamo);
                libroDAO.actualizarStock(connection, libroId, stock - 1);
                prestamoDAO.registrarHistorial(connection, prestamo.getId(), "PRESTAMO",
                        "Préstamo registrado. Stock descontado. Vence el " + prestamo.getFechaVencimiento());
                connection.commit();
                return prestamo;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public synchronized boolean devolverPrestamo(int prestamoId) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try {
                Prestamo prestamo = prestamoDAO.buscarActivoParaActualizar(connection, prestamoId)
                        .orElseThrow(() -> new SQLException("El préstamo no existe o ya fue devuelto."));
                LocalDate hoy = LocalDate.now();
                boolean atrasado = hoy.isAfter(prestamo.getFechaVencimiento());
                EstadoPrestamo estado = atrasado ? EstadoPrestamo.ATRASADO : EstadoPrestamo.DEVUELTO;

                int stock = libroDAO.obtenerStockParaActualizar(connection, prestamo.getLibroId());
                prestamoDAO.marcarDevuelto(connection, prestamoId, hoy, estado);
                libroDAO.actualizarStock(connection, prestamo.getLibroId(), stock + 1);
                prestamoDAO.registrarHistorial(connection, prestamoId, "DEVOLUCION",
                        atrasado ? "Devolución atrasada; stock repuesto." : "Devolución a tiempo; stock repuesto.");
                connection.commit();
                return atrasado;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public static LocalDate calcularVencimiento(LocalDate fechaPrestamo) {
        return fechaPrestamo.plusDays(DIAS_PRESTAMO);
    }

    private void validarEstudianteActivo(Connection connection, int estudianteId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id FROM estudiantes WHERE id=? AND activo=TRUE")) {
            statement.setInt(1, estudianteId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) throw new SQLException("El estudiante no existe o está inactivo.");
            }
        }
    }

    private void validarSinPrestamoDuplicado(Connection connection, int estudianteId, int libroId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id FROM prestamos WHERE estudiante_id=? AND libro_id=? AND fecha_devolucion IS NULL FOR UPDATE")) {
            statement.setInt(1, estudianteId);
            statement.setInt(2, libroId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) throw new SQLException("El estudiante ya tiene este libro en préstamo.");
            }
        }
    }
}

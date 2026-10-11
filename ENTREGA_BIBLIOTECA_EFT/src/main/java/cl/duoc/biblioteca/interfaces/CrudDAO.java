package cl.duoc.biblioteca.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Contrato funcional común para los DAO con CRUD completo. */
public interface CrudDAO<T> {
    T crear(T entidad) throws SQLException;
    List<T> listar() throws SQLException;
    Optional<T> buscarPorId(int id) throws SQLException;
    boolean actualizar(T entidad) throws SQLException;
    boolean eliminar(int id) throws SQLException;
}

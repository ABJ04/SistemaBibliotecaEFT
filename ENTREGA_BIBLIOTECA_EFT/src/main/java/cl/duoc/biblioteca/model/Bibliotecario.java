package cl.duoc.biblioteca.model;

public final class Bibliotecario extends Usuario {
    public Bibliotecario(int id, String username, String nombreCompleto) {
        super(id, username, nombreCompleto, Rol.BIBLIOTECARIO);
    }

    @Override public boolean puedeGestionarCatalogo() { return true; }
    @Override public boolean puedeVerReportes() { return true; }
    @Override public boolean puedeOperarPrestamoDe(int estudianteId) { return true; }
}

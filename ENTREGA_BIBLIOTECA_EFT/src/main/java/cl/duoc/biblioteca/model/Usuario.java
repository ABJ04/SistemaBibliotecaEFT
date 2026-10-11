package cl.duoc.biblioteca.model;

/** Clase abstracta base para demostrar abstracción, herencia y polimorfismo. */
public abstract class Usuario {
    private int id;
    private String username;
    private String nombreCompleto;
    private Rol rol;

    protected Usuario(int id, String username, String nombreCompleto, Rol rol) {
        this.id = id;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public abstract boolean puedeGestionarCatalogo();
    public abstract boolean puedeVerReportes();
    public abstract boolean puedeOperarPrestamoDe(int estudianteId);

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public Integer getEstudianteId() { return null; }
}

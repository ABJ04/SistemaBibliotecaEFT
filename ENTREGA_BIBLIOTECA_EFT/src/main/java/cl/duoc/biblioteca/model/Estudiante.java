package cl.duoc.biblioteca.model;

public final class Estudiante extends Usuario {
    private int estudianteId;
    private String rut;
    private String nombres;
    private String apellidos;
    private String email;
    private String curso;
    private boolean activo;
    private String passwordTemporal;

    public Estudiante() {
        super(0, "", "", Rol.ESTUDIANTE);
        this.activo = true;
    }

    public Estudiante(int usuarioId, String username, int estudianteId, String rut,
                      String nombres, String apellidos, String email, String curso, boolean activo) {
        super(usuarioId, username, nombres + " " + apellidos, Rol.ESTUDIANTE);
        this.estudianteId = estudianteId;
        this.rut = rut;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.curso = curso;
        this.activo = activo;
    }

    @Override public boolean puedeGestionarCatalogo() { return false; }
    @Override public boolean puedeVerReportes() { return false; }
    @Override public boolean puedeOperarPrestamoDe(int id) { return estudianteId == id; }
    @Override public Integer getEstudianteId() { return estudianteId; }

    public int getEstudianteIdValor() { return estudianteId; }
    public void setEstudianteId(int estudianteId) { this.estudianteId = estudianteId; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; setNombreCompleto(nombres + " " + apellidos); }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; setNombreCompleto(nombres + " " + apellidos); }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getPasswordTemporal() { return passwordTemporal; }
    public void setPasswordTemporal(String passwordTemporal) { this.passwordTemporal = passwordTemporal; }
}

package cl.duoc.biblioteca.model;

public final class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String editorial;
    private String isbn;
    private int stock;
    private int categoriaId;
    private String categoriaNombre;

    public Libro() {}
    public Libro(int id, String titulo, String autor, String editorial, String isbn,
                 int stock, int categoriaId, String categoriaNombre) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.isbn = isbn;
        this.stock = stock;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
    }
    public boolean estaDisponible() { return stock > 0; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
}

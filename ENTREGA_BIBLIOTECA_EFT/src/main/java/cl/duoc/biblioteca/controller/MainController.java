package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.dao.*;
import cl.duoc.biblioteca.model.Usuario;
import cl.duoc.biblioteca.service.PrestamoService;
import cl.duoc.biblioteca.view.*;

public final class MainController {
    private final MainView view;

    public MainController(Usuario usuario) {
        this.view = new MainView(usuario);
        view.getLogoutButton().addActionListener(e -> {
            view.dispose();
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
        LibroDAO libroDAO = new LibroDAO();
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        PrestamoDAO prestamoDAO = new PrestamoDAO();

        LibroPanel libros = new LibroPanel(usuario.puedeGestionarCatalogo());
        LibroController libroController = new LibroController(
                libros, libroDAO, categoriaDAO, usuario.puedeGestionarCatalogo());
        view.addTab("Libros", libros);

        if (usuario.puedeGestionarCatalogo()) {
            EstudiantePanel estudiantes = new EstudiantePanel();
            new EstudianteController(estudiantes, new EstudianteDAO());
            view.addTab("Estudiantes", estudiantes);

            CategoriaPanel categorias = new CategoriaPanel();
            new CategoriaController(categorias, categoriaDAO);
            view.addTab("Categorías", categorias);
        }

        PrestamoPanel prestamos = new PrestamoPanel(usuario.getEstudianteId());
        new PrestamoController(prestamos, usuario, prestamoDAO,
                new PrestamoService(prestamoDAO, libroDAO), libroController::refresh);
        view.addTab("Préstamos y devoluciones", prestamos);

        if (usuario.puedeVerReportes()) {
            ReportePanel reportes = new ReportePanel();
            new ReporteController(reportes, new ReporteDAO());
            view.addTab("Reportes", reportes);
        }
    }

    public void show() { view.setVisible(true); }
}

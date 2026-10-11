package cl.duoc.biblioteca;

import cl.duoc.biblioteca.model.Bibliotecario;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.model.Usuario;
import cl.duoc.biblioteca.service.PrestamoService;
import cl.duoc.biblioteca.util.PasswordUtils;

import java.time.LocalDate;

/** Pruebas sin dependencias externas; ejecutar con -ea. */
public final class PruebasBasicas {
    public static void main(String[] args) {
        LocalDate inicio = LocalDate.of(2026, 10, 9);
        assert PrestamoService.calcularVencimiento(inicio).equals(LocalDate.of(2026, 10, 23));
        assert PasswordUtils.sha256("abc").equals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad");

        Usuario bibliotecario = new Bibliotecario(1, "biblio", "Bibliotecario");
        Usuario estudiante = new Estudiante(2, "alumno", 7, "1-9", "Ana", "Pérez", "a@b.cl", "4A", true);
        assert bibliotecario.puedeGestionarCatalogo();
        assert bibliotecario.puedeOperarPrestamoDe(999);
        assert !estudiante.puedeGestionarCatalogo();
        assert estudiante.puedeOperarPrestamoDe(7);
        assert !estudiante.puedeOperarPrestamoDe(8);
        System.out.println("Pruebas básicas OK");
    }
}

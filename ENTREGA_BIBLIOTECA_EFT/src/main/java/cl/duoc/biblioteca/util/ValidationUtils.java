package cl.duoc.biblioteca.util;

public final class ValidationUtils {
    private ValidationUtils() {}

    public static String required(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " es obligatorio.");
        return value.trim();
    }

    public static int positiveInt(String value, String field, boolean allowZero) {
        try {
            int number = Integer.parseInt(required(value, field));
            if (allowZero ? number < 0 : number <= 0) throw new NumberFormatException();
            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " debe ser un número " + (allowZero ? "mayor o igual a cero." : "positivo."));
        }
    }
}

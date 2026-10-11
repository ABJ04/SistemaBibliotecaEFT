package cl.duoc.biblioteca.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Singleton que centraliza la configuración y creación de conexiones JDBC. */
public final class DatabaseConnection {
    private static volatile DatabaseConnection instance;

    private final String url;
    private final String user;
    private final String password;

    private DatabaseConnection() {
        this.url = value("BIBLIOTECA_DB_URL",
                "jdbc:mysql://localhost:3306/biblioteca_escolar?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Santiago");
        this.user = value("BIBLIOTECA_DB_USER", "root");
        this.password = value("BIBLIOTECA_DB_PASSWORD", "");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontró MySQL Connector/J en lib.", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private static String value(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) return systemValue;
        String environmentValue = System.getenv(key);
        return environmentValue == null || environmentValue.isBlank() ? defaultValue : environmentValue;
    }
}

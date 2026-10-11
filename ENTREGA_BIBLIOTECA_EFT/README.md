# Sistema de Biblioteca Escolar

Aplicación de escritorio Java Swing para gestionar libros, categorías, estudiantes, préstamos, devoluciones y reportes con persistencia MySQL mediante JDBC.

## Inicio rápido

1. En MySQL Workbench ejecuta `sql/Script_crea_tablas_biblioteca.sql`.
2. Ejecuta `sql/Script_poblado_tablas_biblioteca.sql`.
3. Abre esta carpeta completa en IntelliJ IDEA.
4. Configura un JDK 17 o superior.
5. Si tu usuario MySQL no es `root` sin contraseña, define en la configuración de ejecución:
   - `BIBLIOTECA_DB_USER`
   - `BIBLIOTECA_DB_PASSWORD`
   - opcionalmente `BIBLIOTECA_DB_URL`
6. Ejecuta `cl.duoc.biblioteca.Main`.

También puedes usar `EJECUTAR.bat` después de configurar la base de datos. El JAR compilado utiliza `dist/lib/mysql-connector-j-26.7.0.jar`.

## Credenciales de prueba

- Bibliotecario: `bibliotecario` / `Biblioteca123!`
- Estudiante: `estudiante1` / `Estudiante123!`


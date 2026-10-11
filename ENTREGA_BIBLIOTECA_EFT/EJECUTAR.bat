@echo off
setlocal
cd /d "%~dp0"
if not exist "dist\BibliotecaEFT.jar" (
  echo No existe dist\BibliotecaEFT.jar. Abre el proyecto en IntelliJ y ejecuta cl.duoc.biblioteca.Main.
  pause
  exit /b 1
)
java -cp "dist\BibliotecaEFT.jar;dist\lib\mysql-connector-j-26.7.0.jar" cl.duoc.biblioteca.Main
if errorlevel 1 pause

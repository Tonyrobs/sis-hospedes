@echo off
REM Script para iniciar a aplicação no Windows

cd /d "%~dp0"

echo.
echo ==========================================
echo Sistema de Reserva de Hotel
echo CRUD de Hospedes
echo ==========================================
echo.

echo Compilando e empacotando o projeto...
call mvn clean package -DskipTests

if %ERRORLEVEL% equ 0 (
    echo.
    echo Projeto compilado com sucesso!
    echo.
    echo Iniciando a aplicacao...
    echo.
    java -jar target\sistema-0.0.1-SNAPSHOT.jar
) else (
    echo.
    echo Erro na compilacao. Por favor, verifique o arquivo pom.xml
    pause
    exit /b 1
)

pause

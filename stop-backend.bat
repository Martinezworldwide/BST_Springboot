@echo off
REM Stop Spring Boot backend: kill process listening on port 8080 (Windows)
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do taskkill /F /PID %%a 2>nul
echo Backend stop requested (port 8080).

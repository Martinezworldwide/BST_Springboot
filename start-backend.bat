@echo off
REM Start Spring Boot backend (SecureBank BST API). Uses Maven wrapper if present.
cd /d "%~dp0backend"
if exist mvnw.cmd (call mvnw.cmd spring-boot:run) else (call mvn spring-boot:run)

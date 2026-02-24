#!/bin/sh
# Stop Spring Boot backend: kill process listening on port 8080 (Mac/Linux)
pid=$(lsof -ti:8080); [ -n "$pid" ] && kill -9 $pid 2>/dev/null; true
echo Backend stop requested (port 8080).

#!/bin/sh
# Start Spring Boot backend (SecureBank BST API). Uses Maven wrapper if present.
cd "$(dirname "$0")/backend"
if [ -x ./mvnw ]; then exec ./mvnw spring-boot:run; else exec mvn spring-boot:run; fi

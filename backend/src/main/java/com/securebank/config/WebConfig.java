package com.securebank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration so GitHub Pages frontend can call the Render-deployed API.
 * Covers /api/** and /actuator/** (health check).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String DEFAULT_ORIGINS =
            "http://localhost:8080,http://127.0.0.1:5500,https://martinezworldwide.github.io,https://*.github.io";

    @Value("${cors.allowed-origins:}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Use env CORS_ORIGINS if set; otherwise allow localhost + GitHub Pages (avoids ${var:http://...} colon parsing)
        String originsStr = (allowedOrigins == null || allowedOrigins.isBlank()) ? DEFAULT_ORIGINS : allowedOrigins.trim();
        String[] origins = originsStr.split("\\s*,\\s*");

        // Apply CORS to API and to actuator (health check used by frontend)
        String[] mappings = { "/api/**", "/actuator/**" };
        for (String mapping : mappings) {
            registry.addMapping(mapping)
                    .allowedOriginPatterns(origins)
                    .allowedMethods("GET", "POST", "DELETE", "OPTIONS", "HEAD")
                    .allowedHeaders("*")
                    .maxAge(3600);
        }
    }
}

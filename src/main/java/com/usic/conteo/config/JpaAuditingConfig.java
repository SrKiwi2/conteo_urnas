package com.usic.conteo.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {
        // Devuelve siempre el usuario 1 (puedes cambiarlo cuando integres seguridad real)
    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> Optional.of(1L);
    }
}

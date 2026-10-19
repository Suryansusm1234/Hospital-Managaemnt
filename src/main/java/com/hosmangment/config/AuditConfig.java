package com.hosmangment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "offsetDateTimeProvider")
public class AuditConfig {

    @Bean(name = "offsetDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
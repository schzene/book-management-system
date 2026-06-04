package com.example.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Cache configuration class
 * Enables caching in the application
 */
@Configuration
@EnableCaching
public class CacheConfig {
    // Caching is enabled via @EnableCaching annotation
    // Cache behavior is controlled through service layer annotations
}
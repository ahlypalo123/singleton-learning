package ru.iubip.config;

/** Reads settings from environment variables for production-like startup. */
public final class EnvironmentConfigurationSource implements ConfigurationSource {
    @Override
    public ConfigurationData load() {
        String provider = System.getenv().getOrDefault("NOTIFICATION_PROVIDER", "email");
        boolean auditEnabled = Boolean.parseBoolean(System.getenv().getOrDefault("AUDIT_ENABLED", "true"));
        return new ConfigurationData(provider, auditEnabled);
    }
}

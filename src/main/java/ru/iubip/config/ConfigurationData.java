package ru.iubip.config;

import java.util.Objects;

/** Immutable application settings used by notification services. */
public final class ConfigurationData {
    private final String notificationProviderType;
    private final boolean auditEnabled;

    public ConfigurationData(String notificationProviderType, boolean auditEnabled) {
        this.notificationProviderType = Objects.requireNonNull(notificationProviderType);
        this.auditEnabled = auditEnabled;
    }

    public String notificationProviderType() {
        return notificationProviderType;
    }

    public boolean auditEnabled() {
        return auditEnabled;
    }
}

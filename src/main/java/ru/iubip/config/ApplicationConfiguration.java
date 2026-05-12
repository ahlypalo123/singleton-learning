package ru.iubip.config;

import java.util.Objects;

/**
 * Классический потокобезопасный Singleton («КонфигурацияПриложения») с ленивой инициализацией.
 * Тестовые методы показывают цену глобального состояния: источник и экземпляр приходится сбрасывать вручную.
 */
public final class ApplicationConfiguration {
    private static volatile ApplicationConfiguration instance;
    private static volatile ConfigurationSource source = new EnvironmentConfigurationSource();

    private final ConfigurationData data;

    private ApplicationConfiguration(ConfigurationSource source) {
        this.data = source.load();
    }

    public static ApplicationConfiguration getInstance() {
        ApplicationConfiguration local = instance;
        if (local == null) {
            synchronized (ApplicationConfiguration.class) {
                local = instance;
                if (local == null) {
                    local = new ApplicationConfiguration(source);
                    instance = local;
                }
            }
        }
        return local;
    }

    public String notificationProviderType() {
        return data.notificationProviderType();
    }

    public boolean auditEnabled() {
        return data.auditEnabled();
    }

    public static synchronized void setSourceForTests(ConfigurationSource testSource) {
        source = Objects.requireNonNull(testSource);
    }

    public static synchronized void resetForTests() {
        instance = null;
        source = new EnvironmentConfigurationSource();
    }
}

package ru.iubip.di;

import ru.iubip.audit.InMemoryNotificationAuditor;
import ru.iubip.audit.NotificationAuditor;
import ru.iubip.config.ConfigurationData;
import ru.iubip.config.ConfigurationSource;
import ru.iubip.notification.InjectedNotificationService;

/** Manual composition root: one configuration and one auditor per application lifecycle, without global access. */
public final class ApplicationContainer {
    private final ConfigurationData configuration;
    private final InMemoryNotificationAuditor auditor;
    private final InjectedNotificationService notificationService;

    public ApplicationContainer(ConfigurationSource source) {
        this.configuration = source.load();
        this.auditor = new InMemoryNotificationAuditor();
        this.notificationService = new InjectedNotificationService(configuration, auditor);
    }

    public ConfigurationData configuration() {
        return configuration;
    }

    public NotificationAuditor auditor() {
        return auditor;
    }

    public InjectedNotificationService notificationService() {
        return notificationService;
    }
}

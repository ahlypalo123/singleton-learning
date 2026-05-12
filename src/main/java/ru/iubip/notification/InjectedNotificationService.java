package ru.iubip.notification;

import java.util.Objects;
import ru.iubip.audit.AuditRecord;
import ru.iubip.audit.NotificationAuditor;
import ru.iubip.config.ConfigurationData;

/** Business service that receives lifecycle-managed dependencies from outside. */
public final class InjectedNotificationService {
    private final ConfigurationData configuration;
    private final NotificationAuditor auditor;

    public InjectedNotificationService(ConfigurationData configuration, NotificationAuditor auditor) {
        this.configuration = Objects.requireNonNull(configuration);
        this.auditor = Objects.requireNonNull(auditor);
    }

    public NotificationResult send(NotificationRequest request) {
        String channel = configuration.notificationProviderType();
        NotificationResult result = new NotificationResult(channel, true, "Sent via " + channel);

        if (configuration.auditEnabled()) {
            auditor.record(new AuditRecord(
                    request.sender(),
                    request.recipient(),
                    request.appealId(),
                    channel,
                    "SUCCESS"
            ));
        }

        return result;
    }
}

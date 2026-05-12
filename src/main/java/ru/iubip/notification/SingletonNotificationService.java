package ru.iubip.notification;

import ru.iubip.audit.AuditRecord;
import ru.iubip.audit.SingletonNotificationAuditor;
import ru.iubip.config.ApplicationConfiguration;

/** Business service intentionally coupled to global Singleton access. */
public final class SingletonNotificationService {
    public NotificationResult send(NotificationRequest request) {
        ApplicationConfiguration configuration = ApplicationConfiguration.getInstance();
        String channel = configuration.notificationProviderType();
        NotificationResult result = new NotificationResult(channel, true, "Sent via " + channel);

        if (configuration.auditEnabled()) {
            SingletonNotificationAuditor.getInstance().record(new AuditRecord(
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

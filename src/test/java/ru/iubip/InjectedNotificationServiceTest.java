package ru.iubip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import ru.iubip.audit.InMemoryNotificationAuditor;
import ru.iubip.config.ConfigurationData;
import ru.iubip.di.ApplicationContainer;
import ru.iubip.notification.InjectedNotificationService;
import ru.iubip.notification.NotificationRequest;

final class InjectedNotificationServiceTest {
    private static final NotificationRequest REQUEST = new NotificationRequest(
            "dean@example.edu",
            "student@example.edu",
            "AP-42",
            "Ваше обращение принято"
    );

    @Test
    void injectedServicesAreIndependentWithoutStaticReset() {
        InMemoryNotificationAuditor emailAudit = new InMemoryNotificationAuditor();
        InMemoryNotificationAuditor smsAudit = new InMemoryNotificationAuditor();
        InjectedNotificationService emailService = new InjectedNotificationService(
                new ConfigurationData("email", true),
                emailAudit
        );
        InjectedNotificationService smsService = new InjectedNotificationService(
                new ConfigurationData("sms", true),
                smsAudit
        );

        assertEquals("email", emailService.send(REQUEST).channel());
        assertEquals("sms", smsService.send(REQUEST).channel());
        assertEquals(1, emailAudit.records().size());
        assertEquals(1, smsAudit.records().size());
        assertNotSame(emailAudit, smsAudit);
    }

    @Test
    void configurationSourceIsSubstitutedByPassingDependency() {
        ApplicationContainer container = new ApplicationContainer(() -> new ConfigurationData("push", false));

        assertEquals("push", container.notificationService().send(REQUEST).channel());
        assertEquals(0, container.auditor().records().size(),
                "No static hooks are needed: the test passes a source to the composition root");
    }

    @Test
    void containerKeepsSingleInstancesWithinApplicationLifecycle() {
        ApplicationContainer container = new ApplicationContainer(() -> new ConfigurationData("email", true));

        assertSame(container.configuration(), container.configuration());
        assertSame(container.auditor(), container.auditor());
        assertSame(container.notificationService(), container.notificationService());
    }
}

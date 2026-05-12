package ru.iubip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.iubip.audit.SingletonNotificationAuditor;
import ru.iubip.config.ApplicationConfiguration;
import ru.iubip.config.ConfigurationData;
import ru.iubip.config.ConfigurationSource;
import ru.iubip.notification.NotificationRequest;
import ru.iubip.notification.SingletonNotificationService;

final class SingletonNotificationServiceTest {
    private static final NotificationRequest REQUEST = new NotificationRequest(
            "dean@example.edu",
            "student@example.edu",
            "AP-42",
            "Ваше обращение принято"
    );

    @AfterEach
    void resetGlobalState() {
        ApplicationConfiguration.resetForTests();
        SingletonNotificationAuditor.resetForTests();
    }

    @Test
    void singletonStateRequiresExplicitResetForIndependentTests() {
        ApplicationConfiguration.setSourceForTests(() -> new ConfigurationData("email", true));
        assertEquals("email", new SingletonNotificationService().send(REQUEST).channel());

        ApplicationConfiguration.setSourceForTests(() -> new ConfigurationData("sms", true));

        assertEquals("email", new SingletonNotificationService().send(REQUEST).channel(),
                "Already initialized singleton ignores a new source until reset, so tests must clean global state");

        ApplicationConfiguration.resetForTests();
        ApplicationConfiguration.setSourceForTests(() -> new ConfigurationData("sms", true));
        assertEquals("sms", new SingletonNotificationService().send(REQUEST).channel());
    }

    @Test
    void singletonSourceCanBeSubstitutedOnlyThroughTestHook() {
        ApplicationConfiguration.setSourceForTests(() -> new ConfigurationData("push", false));

        assertEquals("push", new SingletonNotificationService().send(REQUEST).channel());
        assertEquals(0, SingletonNotificationAuditor.getInstance().records().size(),
                "Audit flag from substituted configuration is used, but substitution depends on static test-only API");
    }

    @Test
    void singletonLazyInitializationIsThreadSafe() throws Exception {
        AtomicInteger loads = new AtomicInteger();
        ConfigurationSource source = () -> {
            loads.incrementAndGet();
            return new ConfigurationData("email", true);
        };
        ApplicationConfiguration.setSourceForTests(source);

        var executor = Executors.newFixedThreadPool(8);
        try {
            List<Callable<ApplicationConfiguration>> tasks = new ArrayList<>();
            for (int i = 0; i < 40; i++) {
                tasks.add(ApplicationConfiguration::getInstance);
            }

            List<ApplicationConfiguration> configurations = executor.invokeAll(tasks).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception exception) {
                            throw new AssertionError(exception);
                        }
                    })
                    .toList();

            ApplicationConfiguration first = configurations.get(0);
            configurations.forEach(configuration -> assertSame(first, configuration));
        } finally {
            executor.shutdownNow();
        }
        assertEquals(1, loads.get());
    }
}

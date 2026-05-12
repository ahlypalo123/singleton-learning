package ru.iubip.audit;

/** Singleton-аудитор для варианта с глобальным доступом. */
public final class SingletonNotificationAuditor {
    private static final InMemoryNotificationAuditor INSTANCE = new InMemoryNotificationAuditor();

    private SingletonNotificationAuditor() {
    }

    public static InMemoryNotificationAuditor getInstance() {
        return INSTANCE;
    }

    public static void resetForTests() {
        INSTANCE.clear();
    }
}

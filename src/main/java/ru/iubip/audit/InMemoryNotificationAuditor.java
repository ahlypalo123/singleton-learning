package ru.iubip.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** In-memory implementation that can later be replaced by file or database storage. */
public final class InMemoryNotificationAuditor implements NotificationAuditor {
    private final List<AuditRecord> records = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void record(AuditRecord record) {
        records.add(record);
    }

    @Override
    public List<AuditRecord> records() {
        synchronized (records) {
            return List.copyOf(records);
        }
    }

    public void clear() {
        records.clear();
    }
}

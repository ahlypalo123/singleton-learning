package ru.iubip.audit;

import java.util.List;

public interface NotificationAuditor {
    void record(AuditRecord record);

    List<AuditRecord> records();
}

package ru.iubip.audit;

public record AuditRecord(
        String sender,
        String recipient,
        String appealId,
        String channel,
        String result
) {
}

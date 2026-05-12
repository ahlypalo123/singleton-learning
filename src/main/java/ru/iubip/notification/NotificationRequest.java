package ru.iubip.notification;

public record NotificationRequest(
        String sender,
        String recipient,
        String appealId,
        String text
) {
}

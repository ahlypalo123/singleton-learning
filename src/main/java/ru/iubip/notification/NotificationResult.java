package ru.iubip.notification;

public record NotificationResult(String channel, boolean success, String message) {
}

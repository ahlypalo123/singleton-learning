package ru.iubip.config;

/** Replaceable source for application configuration. */
public interface ConfigurationSource {
    ConfigurationData load();
}

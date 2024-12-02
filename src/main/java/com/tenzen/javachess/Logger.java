package com.tenzen.javachess;

public class Logger {
    public static LogLevel logLevel = LogLevel.NONE;

    public enum LogLevel {
        NONE,
        ERROR,
        WARNING,
        INFO,
        DEBUG,
        TRACE,
    }

    public static void trace(String message) {
        if (logLevel == LogLevel.TRACE) {
            System.out.println("[TRACE] " + message);
        }
    }

    public static void debug(String message) {
        if (logLevel == LogLevel.TRACE || logLevel == LogLevel.DEBUG) {
            System.out.println("[DEBUG] " + message);
        }
    }

    public static void info(String message) {
        if (logLevel == LogLevel.TRACE || logLevel == LogLevel.DEBUG || logLevel == LogLevel.INFO) {
            System.out.println("[INFO] " + message);
        }
    }

    public static void warning(String message) {
        if (logLevel == LogLevel.TRACE || logLevel == LogLevel.DEBUG || logLevel == LogLevel.INFO || logLevel == LogLevel.WARNING) {
            System.err.println("[WARNING] " + message);
        }
    }

    public static void error(String message) {
        if (logLevel == LogLevel.TRACE || logLevel == LogLevel.DEBUG || logLevel == LogLevel.INFO || logLevel == LogLevel.WARNING || logLevel == LogLevel.ERROR) {
            System.err.println("[Error] " + message);
        }
    }
}

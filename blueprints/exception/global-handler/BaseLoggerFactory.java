package com.example.shared.api.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLoggerFactory {
    public static Logger getBaseLogger() {
        return LoggerFactory.getLogger("AppLogger");
    }
}

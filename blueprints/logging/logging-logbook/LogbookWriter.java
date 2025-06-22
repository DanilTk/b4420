package com.example.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Precorrelation;

public class LogbookWriter implements HttpLogWriter {
    private final Logger requestLogger = LoggerFactory.getLogger(LoggerName.REQUEST_LOGGER.name());
    private final Logger responseLogger = LoggerFactory.getLogger(LoggerName.RESPONSE_LOGGER.name());

    @Override
    public void write(Precorrelation precorrelation, String request) {
        requestLogger.info(request);
    }

    @Override
    public void write(Correlation correlation, String response) {
        responseLogger.info(response);
    }
}

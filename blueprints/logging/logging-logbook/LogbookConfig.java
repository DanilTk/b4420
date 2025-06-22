package com.example.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.json.JsonHttpLogFormatter;

@Configuration
public class LogbookConfig {

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
            .sink(new DefaultSink(new JsonHttpLogFormatter(), new LogbookWriter()))
            .build();
    }
}

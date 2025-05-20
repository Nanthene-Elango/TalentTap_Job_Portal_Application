package com.talenttap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Configuration
public class DateFormatConfiguration {

    @Bean
    public FormattingConversionServiceFactoryBean conversionService() {
        FormattingConversionServiceFactoryBean factory = new FormattingConversionServiceFactoryBean();
        Set<Formatter<?>> formatters = new HashSet<>();
        formatters.add(new LocalDateFormatter());
        factory.setFormatters(formatters);
        return factory;
    }

    private static class LocalDateFormatter implements Formatter<LocalDate> {
        @Override
        public LocalDate parse(String text, Locale locale) throws ParseException {
            return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Override
        public String print(LocalDate date, Locale locale) {
            return date.format(DateTimeFormatter.ISO_LOCAL_DATE); // Always yyyy-MM-dd
        }
    }
}
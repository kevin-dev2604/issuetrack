package com.kevinj.portfolio.issuetrack.global.config;

import com.kevinj.portfolio.issuetrack.global.time.DateTimeFormats;
import org.springframework.boot.jackson.JacksonComponent;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JacksonComponent
public class LocalDateTimeSerializerConfig extends StdSerializer<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormats.DEFAULT_DATE_TIME);

    public LocalDateTimeSerializerConfig() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
        gen.writeString(value.format(formatter));
    }
}

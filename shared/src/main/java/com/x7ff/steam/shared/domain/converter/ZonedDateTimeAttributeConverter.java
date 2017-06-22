package com.x7ff.steam.shared.domain.converter;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public final class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime time) {
        return convertToTimestamp(time);
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
    }

    public static Timestamp convertToTimestamp(ZonedDateTime time) {
        return time == null ? null : Timestamp.valueOf(time.toLocalDateTime());
    }

}
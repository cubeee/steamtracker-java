package com.x7ff.steam.domain.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public final class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime time) {
		return time == null ? null : Timestamp.valueOf(time);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toLocalDateTime();
	}
}
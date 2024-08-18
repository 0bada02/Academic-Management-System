package ju.Model.Class.Enum.DaysOfWeek;

import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class DaysOfWeekConverter implements AttributeConverter<List<DaysOfWeek>, String> {

    @Override
    public String convertToDatabaseColumn(List<DaysOfWeek> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(DaysOfWeek::getDayName) // Use getDayName for string representation
                .collect(Collectors.joining(","));
    }

    @Override
    public List<DaysOfWeek> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(dbData.split(","))
                .map(String::trim)
                .map(dayName -> {
                    for (DaysOfWeek day : DaysOfWeek.values()) {
                        if (day.getDayName().equalsIgnoreCase(dayName)) {
                            return day;
                        }
                    }
                    throw new IllegalArgumentException("Unknown day name: " + dayName);
                })
                .collect(Collectors.toList());
    }
}
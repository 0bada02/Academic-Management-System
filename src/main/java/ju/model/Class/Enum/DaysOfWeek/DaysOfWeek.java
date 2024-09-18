package ju.model.Class.Enum.DaysOfWeek;

import lombok.*;

@AllArgsConstructor
@ToString
@Getter
public enum DaysOfWeek {
    SUNDAY("Sunday"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday");

    private final String dayName;
}
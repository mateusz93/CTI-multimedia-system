package pl.lodz.p.cti.utils;

import lombok.AllArgsConstructor;
import pl.lodz.p.cti.exceptions.IncorrectTimeException;

import java.time.LocalTime;
import java.util.StringTokenizer;

@AllArgsConstructor
public class StringToLocalTimeConverter {

    private static final String DELIMITER = ":";
    private final String rawTime;

    public LocalTime getLocalTime() throws IncorrectTimeException {
        if (!rawTime.contains(DELIMITER)) {
            throw new IncorrectTimeException(rawTime);
        }
        StringTokenizer stringTokenizer = new StringTokenizer(rawTime, DELIMITER);
        if (stringTokenizer.countTokens() != 2) {
            throw new IncorrectTimeException(rawTime);
        }
        try {
            int hour = Integer.parseInt(stringTokenizer.nextToken());
            int minute = Integer.parseInt(stringTokenizer.nextToken());
            return LocalTime.MIN.withHour(hour).withMinute(minute);
        } catch (NumberFormatException e) {
            throw new IncorrectTimeException(rawTime);
        }
    }
}

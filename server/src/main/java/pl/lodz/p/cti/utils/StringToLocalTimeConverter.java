package pl.lodz.p.cti.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.lodz.p.cti.exceptions.IncorrectTimeException;

import java.time.LocalTime;
import java.util.StringTokenizer;

@Slf4j
@AllArgsConstructor
public class StringToLocalTimeConverter {

    private static final String DELIMITER = ":";
    private final String rawTime;

    public LocalTime getLocalTime() throws IncorrectTimeException {
        log.debug("Getting local time");
        if (!rawTime.contains(DELIMITER)) {
            log.error("Incorrect time: {}", rawTime);
            throw new IncorrectTimeException(rawTime);
        }
        StringTokenizer stringTokenizer = new StringTokenizer(rawTime, DELIMITER);
        if (stringTokenizer.countTokens() != 2) {
            log.error("Incorrect time: {}", rawTime);
            throw new IncorrectTimeException(rawTime);
        }
        try {
            int hour = Integer.parseInt(stringTokenizer.nextToken());
            int minute = Integer.parseInt(stringTokenizer.nextToken());
            return LocalTime.MIN.withHour(hour).withMinute(minute);
        } catch (NumberFormatException e) {
            log.error("Incorrect time: {}", rawTime);
            throw new IncorrectTimeException(rawTime);
        }
    }
}

package pl.lodz.p.cti.exceptions;

public class IncorrectTimeException extends ValidationException {

    private static final String TIME_PARAM = "TIME";
    private static final String CORRECT_TIME_FORMAT = "HH:MM";
    private static final String ERROR = "Invalid format of time. Given: " + TIME_PARAM
            + ", expected: " + CORRECT_TIME_FORMAT;

    public IncorrectTimeException(String time) {
        super(ERROR.replaceFirst(TIME_PARAM, time));
    }
}

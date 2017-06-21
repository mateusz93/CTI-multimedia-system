package pl.lodz.p.cti.exceptions;

public class IncorrectTimeException extends ValidationException {
    private static String TIME_PARAM = "TIME";

    private static String CORRECT_TIME_FORMAT = "HH:MM";

    private final static String error = "Invalid format of time. Given: "+TIME_PARAM+", expected:"+CORRECT_TIME_FORMAT;

    public IncorrectTimeException(String time){
        super(error.replaceFirst(TIME_PARAM,time));
    }
}

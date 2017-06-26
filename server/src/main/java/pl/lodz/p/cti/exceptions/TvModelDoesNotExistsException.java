package pl.lodz.p.cti.exceptions;

public class TvModelDoesNotExistsException extends ValidationException {

    private static final String FIELD_PARAM = "FIELD";
    private static final String VALUE_PARAM = "VALUE";
    private static final String ERROR = "Tv model with field " + FIELD_PARAM + " = "
            + VALUE_PARAM + " doesn't exists";

    public TvModelDoesNotExistsException(String field, String value) {
        super(ERROR.replaceFirst(FIELD_PARAM, field).replaceFirst(VALUE_PARAM, value));
    }
}

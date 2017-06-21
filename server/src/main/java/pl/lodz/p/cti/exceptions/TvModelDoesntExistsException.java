package pl.lodz.p.cti.exceptions;

public class TvModelDoesntExistsException extends ValidationException {

    private static String FIELD_PARAM = "FIELD";

    private static String VALUE_PARAM = "VALUE";

    private final static String error = "Tv model with field "+FIELD_PARAM+"="+VALUE_PARAM+" doesn't exists";

    public TvModelDoesntExistsException(String field, String value){
        super(error.replaceFirst(FIELD_PARAM,field).replaceFirst(VALUE_PARAM,value));
    }
}

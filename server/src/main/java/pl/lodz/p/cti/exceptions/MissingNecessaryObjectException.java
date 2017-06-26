package pl.lodz.p.cti.exceptions;

public class MissingNecessaryObjectException extends ValidationException {

    private static final String ERROR = "Missing necessary object data!";

    public MissingNecessaryObjectException() {
        super(ERROR);
    }
}

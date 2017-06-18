package pl.lodz.p.cti.exceptions;

public class MissingNecessaryObjectException extends ValidationException {

    private final static String error = "Missing necessary object data!";

    public MissingNecessaryObjectException(){
        super(error);
    }
}

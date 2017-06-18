package pl.lodz.p.cti.exceptions;

public class UnexpectedErrorException extends ValidationException {

    private final static String error = "Unexpected error!";

    public UnexpectedErrorException(Exception e){
        super(error);
        e.printStackTrace();
    }
}

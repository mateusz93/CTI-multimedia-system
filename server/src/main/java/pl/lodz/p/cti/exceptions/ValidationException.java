package pl.lodz.p.cti.exceptions;

public class ValidationException extends Exception {

    ValidationException(String error){
        super(error);
    }
}

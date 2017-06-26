package pl.lodz.p.cti.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnexpectedErrorException extends ValidationException {

    private static final String ERROR = "Unexpected ERROR!";

    public UnexpectedErrorException(Exception e){
        super(ERROR);
        log.error("Unexpected error occurred ", e);
    }
}

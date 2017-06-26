package pl.lodz.p.cti.exceptions;

public class UnsupportedExtensionException extends ValidationException {

    private static final String EXTENSION_PARAM = "EXTENSION";
    private static final String ERROR = "Unsupported extension exception! Given: "
            + EXTENSION_PARAM + ", expected: jgp/jpeg/png/gif";

    public UnsupportedExtensionException(String extension){
        super(ERROR.replaceFirst(EXTENSION_PARAM, extension));
    }
}


package pl.lodz.p.cti.exceptions;

public class UnsupportedExtensionException  extends ValidationException {

    private static String EXTENSION_PARAM = "EXTENSION";

    private final static String error = "Unsupported extension exception! Given: "+EXTENSION_PARAM+", expected: jgp/jpeg/png/gif";

    public UnsupportedExtensionException(String extension){
        super(error.replaceFirst(EXTENSION_PARAM,extension));
    }
}


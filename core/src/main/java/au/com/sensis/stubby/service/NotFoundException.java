package au.com.sensis.stubby.service;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
    }
}

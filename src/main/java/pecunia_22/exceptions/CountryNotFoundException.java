package pecunia_22.exceptions;

public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(Long id) {
        super("Country not found: " + id);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

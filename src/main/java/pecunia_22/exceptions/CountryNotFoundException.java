package pecunia_22.exceptions;

public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException(Long id) {
        super("Country not found Id: " + id);

    }public CountryNotFoundException(String country) {
        super("Country not found: " + country);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

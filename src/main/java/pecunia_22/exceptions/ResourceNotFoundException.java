package pecunia_22.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Long id) {
        super("Resource not found: " + id);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

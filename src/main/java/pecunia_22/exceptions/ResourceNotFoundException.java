package pecunia_22.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forId(String resource, Long id) {
        return new ResourceNotFoundException(resource + " not found with id: " + id);
    }


    public static ResourceNotFoundException forField(String resource, String field, String value) {
        return new ResourceNotFoundException(resource + " not found with " + field + ": " + value);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

//    public ResourceNotFoundException(Long id) {
//        super("Resource not found: " + id);
//    }
//
//    public ResourceNotFoundException(String country) {
//        super("Resource not found: " + country);
//    }
//
//    @Override
//    public synchronized Throwable fillInStackTrace() {
//        return this;
//    }
}

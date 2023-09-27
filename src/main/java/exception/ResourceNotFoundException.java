package exception;


import com.be.klash.models.Response;


public class ResourceNotFoundException extends ApplicationException {

    private final Response errorCode = Response.RESOURCE_NOT_FOUND;
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);

        setCode(errorCode);
    }
    
    public ResourceNotFoundException( String message) {
        super(message);
        setCode(errorCode);
    }
    
    public ResourceNotFoundException(Response errorCode) {
        super(errorCode.getResponseMessage());
        setCode(errorCode);
    }

}

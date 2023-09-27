package exception;


import com.be.klash.models.Response;


public class ValidationException extends ApplicationException {

    private final Response errorCode = Response.VALIDATION_ERROR;

    
    public ValidationException( String message, Throwable cause) {
        super(message, cause);
        setCode(errorCode);
    }
    


    public ValidationException() {
        super(Response.VALIDATION_ERROR.toString());
        setCode(errorCode);
    }

    public ValidationException(Exception e) {
        super(e);
        setCode(errorCode);
    }

    public ValidationException(String message) {
        super(message);
        setCode(errorCode);
    }
    
    public ValidationException(Response errorCode) {
        super(errorCode.getResponseMessage());
        setCode(errorCode);
    }

}

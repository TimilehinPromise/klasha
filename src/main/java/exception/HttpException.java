package exception;


import com.be.klash.models.ErrorResponse;
import com.be.klash.models.Response;


public class HttpException extends ApplicationException {

    private final Response errorCode = Response.HTTP_ERROR;
   // public HttpException(String message, Throwable cause) {
//        super(message, cause);
//    }
    
    public HttpException( String message, Throwable cause) {
        super(message, cause);
        setCode(errorCode);
    }

    public HttpException() {
        super(Response.HTTP_ERROR.toString());
        setCode(errorCode);
    }

    public HttpException(Exception e) {
        super(e);
        setCode(errorCode);
    }

    public HttpException(String message) {
        super(message);
        setCode(errorCode);
    }
    
    public HttpException( ErrorResponse errorResponse){
        super(errorResponse.getResponseMessage());
        try{
            setCode(Response.valueOf(errorResponse.getResponseCode()));
        }catch (IllegalArgumentException ex){
            setCode(errorCode);
        }
    }
    
    public HttpException( Response errorCode) {
        super(errorCode.getResponseMessage());
        setCode(errorCode);
    }

}

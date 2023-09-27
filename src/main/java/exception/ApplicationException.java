/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;


import com.be.klash.models.ErrorResponse;
import com.be.klash.models.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicationException extends RuntimeException {


    private Response code = Response.SYSTEM_ERROR;
    private ErrorResponse errorResponse;

    public ApplicationException() {
    }

//    public ApplicationException(String message) {
//        super(message);
//    }

//    public ApplicationException(String message, Throwable cause) {
//        super(message, cause);
//    }

    public ApplicationException( String message) {
        super(message);
        setCode(code);
    }

    public ApplicationException( String message, Throwable cause) {
        super(message, cause);
        setCode(Response.SYSTEM_ERROR);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ApplicationException(Response errorCode) {
        super(errorCode.getResponseMessage());
        setCode(errorCode);
    }
}

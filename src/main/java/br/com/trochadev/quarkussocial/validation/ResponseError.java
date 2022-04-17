package br.com.trochadev.quarkussocial.validation;

import lombok.Data;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ResponseError {

    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;

    private String error;
    private Collection<FieldError> errors;

    public ResponseError(String error, Collection<FieldError> errors) {
        this.error = error;
        this.errors = errors;
    }

    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations) {

        return new ResponseError("Validation Error"
                , violations.parallelStream()
                .map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList()));
    }

    public Response withStatusCode(int code){
        return Response.status(code).entity(this).build();
    }
}

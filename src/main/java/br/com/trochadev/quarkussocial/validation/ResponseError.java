package br.com.trochadev.quarkussocial.validation;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ResponseError {
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Collection<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(Collection<FieldError> errors) {
        this.errors = errors;
    }
}

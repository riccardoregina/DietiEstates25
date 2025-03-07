package it.unina.dietiestates25.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleEntityAlreadyExistsException(EntityAlreadyExistsException e,
                                                                           WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDetails(
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.name(),
                        e.getMessage(),
                        request.getDescription(false)));
    }

    @ExceptionHandler(EntityNotExistsException.class)
    public ResponseEntity<ErrorDetails> handleEntityNotExistsException(EntityNotExistsException e,
                                                                       WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDetails(
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.name(),
                        e.getMessage(),
                        request.getDescription(false)));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDetails> handleForbiddenException(ForbiddenException e,
                                                                 WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorDetails(
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN.name(),
                        e.getMessage(),
                        request.getDescription(false)));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetails> handleAuthenticationException(AuthenticationException e,
                                                                    WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDetails(
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.name(),
                        e.getMessage(),
                        request.getDescription(false)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(MethodArgumentNotValidException e,
                                                                   WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDetails(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.name(),
                        e.getMessage(),
                        request.getDescription(false)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception e,
                                                  WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDetails(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.name(),
                        e.getMessage(),
                        request.getDescription(false)));
    }
}

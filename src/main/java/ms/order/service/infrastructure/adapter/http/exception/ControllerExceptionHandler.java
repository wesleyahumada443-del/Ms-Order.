package ms.order.service.infrastructure.adapter.http.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.exceptions.RecordNotFoundException;
import ms.order.service.infrastructure.dto.http.response.ErrorResponse;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;


/**
 * @author Williams Ahumada
 * <p>
 * The ControllerExceptionHandler class is a custom exception handler for controller classes.
 *
 * The class uses the log4j2 logger to log exception details.
 */
@Log4j2
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var response = new ErrorResponse(
                LocalDateTime.now(),
                status,
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        log.error(response, ex);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var fieldErrors = new HashMap<String, String>();
        var status = HttpStatus.BAD_REQUEST;
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
        );

        var response = new ErrorResponse(
                LocalDateTime.now(),
                status,
                "Validation Failed",
                "There are one or more invalid fields",
                request.getRequestURI(),
                fieldErrors
        );

        log.error(response, ex);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        var status = HttpStatus.NOT_FOUND;
        var response = new ErrorResponse(
                LocalDateTime.now(),
                status,
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        log.error(response, ex);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RecordNotFoundException ex, HttpServletRequest request) {
        var status = HttpStatus.NOT_FOUND;
        var response = new ErrorResponse(
                LocalDateTime.now(),
                status,
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        log.error(response, ex);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException ex, HttpServletRequest request) {
        var status = HttpStatus.NOT_FOUND;
        var response = new ErrorResponse(
                LocalDateTime.now(),
                status,
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        log.error(response, ex);
        return ResponseEntity.status(status).body(response);
    }
}



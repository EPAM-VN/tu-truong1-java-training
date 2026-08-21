package local.jt.pet.order.web.configurations;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // ensure your handler is ordered ahead of the one configured by Spring Boot whose order is 0.

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandled(
            Exception ex,
            HttpServletRequest request) {

        // log full stack trace
        log.error("Unhandled exception", ex);

        ErrorResponse response =
                new ErrorResponseException(HttpStatusCode.valueOf(500));

        return ResponseEntity.internalServerError()
                .body(response);
    }
}

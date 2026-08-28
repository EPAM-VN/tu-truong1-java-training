package local.jt.pet.order.web.configurations;

import jakarta.servlet.http.HttpServletRequest;
import local.jt.pet.order.web.exceptions.ApiProblemDetail;
import local.jt.pet.order.web.exceptions.ValidationError;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // ensure your handler is ordered ahead of the one configured by Spring Boot whose order is 0.
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("One or more validation errors occurred.");

        ApiProblemDetail apiProblem = new ApiProblemDetail(problemDetail);

        apiProblem.setErrorCode("VALIDATION_ERROR");
        apiProblem.setTraceId(MDC.get("traceId"));

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        apiProblem.getErrors().add(
                                new ValidationError(
                                        error.getField(),
                                        error.getDefaultMessage()
                                )
                        ));

        return ResponseEntity.badRequest().body(apiProblem);
    }

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

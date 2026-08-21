package local.jt.pet.order.application.exceptions;

import org.springframework.http.ProblemDetail;

import java.time.Instant;

public class ApiProblemDetail extends ProblemDetail {

    private String errorCode;
    private String traceId;
    private Instant timestamp;

    public ApiProblemDetail(ProblemDetail problemDetail) {
        super(problemDetail);
        this.timestamp = Instant.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
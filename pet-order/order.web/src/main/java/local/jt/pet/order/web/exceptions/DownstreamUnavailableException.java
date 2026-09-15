package local.jt.pet.order.web.exceptions;

public class DownstreamUnavailableException extends RuntimeException {
    public DownstreamUnavailableException(String message) {
        super(message);
    }
}
